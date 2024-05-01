import './App.css';
import { useState, useEffect } from 'react';
import { Task } from './Task.js';

function App() {
  const [sortBy, setSortBy] = useState(null);
  const [sortOrder, setSortOrder] = useState('ASC');
  const [todoList, setTodoList] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [editMode, setEditMode] = useState(null);
  const [message, setMessage] = useState(null);
  const [errorMessage, setErrorMessage] = useState(null);

  useEffect(() => {
    fetchTasks();
  }, [sortBy, sortOrder]);

  useEffect(() => {
    const storedTasks = localStorage.getItem('offlineTasks');
    if (storedTasks) {
      setTodoList(JSON.parse(storedTasks));
    }
  }, []);

  const handleChange = (event) => {
    setNewTask(event.target.value);
  }

  const toggleSortOrder = () => {
    setSortOrder(sortOrder === 'ASC' ? 'DESC' : 'ASC');
  };

  // fetch data from server
  const fetchTasks = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/tasks${sortBy ? `?sortBy=${sortBy}&sortOrder=${sortOrder}` : ''}`);
      if(!response.ok) {
        throw new Error('Failed to fetch tasks');
      }

      const data = await response.json();
      const tasksWithIds = data.map((task, index) => ({...task, id:task.id || index}))
      setTodoList(tasksWithIds);
    } catch (error) {
      console.error(error);
      setErrorMessage('Failed to fetch tasks. Please try again.');
    }
  };

  const generateTemporaryId = () => {
    return `temp_${Math.random().toString(36).substr(2, 9)}`;
  };

  // add data and send data to server
  const addTask = async () => {
    if (newTask.trim() === '') return;

    try {
      const response = await fetch('http://localhost:8080/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          taskName: newTask,
          completed: false
        })
      });

      if (response.status === 201) {
        const newTaskWithId = await response.json();
        setTodoList([...todoList, newTaskWithId]);
        setMessage('Task added successfully.');
      } else {
        const temporaryId = generateTemporaryId();
        const taskToAdd = { taskName: newTask, completed: false, id: temporaryId };

        setTodoList([...todoList, taskToAdd]);

        const storedTasks = localStorage.getItem('offlineTasks');
        const tasks = storedTasks ? JSON.parse(storedTasks) : [];
        localStorage.setItem('offlineTasks', JSON.stringify([...tasks, taskToAdd]));

        setMessage('Task added offline. Your new data will be synced when the network is available again');
      }
    } catch (error) {
      console.error(error);

      const temporaryId = generateTemporaryId();
      const taskToAdd = { taskName: newTask, completed: false, id: temporaryId };

      setTodoList([...todoList, taskToAdd]);

      const storedTasks = localStorage.getItem('offlineTasks');
      const tasks = storedTasks ? JSON.parse(storedTasks) : [];
      localStorage.setItem('offlineTasks', JSON.stringify([...tasks, taskToAdd]));

      setErrorMessage('Failed to add task. Please try again.');
    }
  };

  const isServerReachable = async () => {
    try {
      const response = await fetch('http://localhost:8080');
      return response.ok;
    } catch (error) {
      return false;
    }
  };

  const isInternetConnected = () => {
    return navigator.onLine;
  };

  useEffect(() => {
    const syncOfflineTasks = async () => {
      const isServerUp = await isServerReachable();
      const isInternetUp = isInternetConnected();

      if (isServerUp && isInternetUp) {
        const storedTasks = localStorage.getItem('offlineTasks');
        if (storedTasks) {
          const offlineTasks = JSON.parse(storedTasks);
          for (const task of offlineTasks) {
            try {
              const response = await fetch('http://localhost:8080/api/tasks', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                  taskName: task.taskName,
                  completed: task.completed
                })
              });

              if (response.status === 201) {
                const newTaskWithId = await response.json();
                const updatedTasks = todoList.map(t => t.id === task.id ? newTaskWithId : t);
                setTodoList(updatedTasks);
              }
            } catch (error) {
              console.error(error);
            }
          }
          localStorage.removeItem('offlineTasks');
        }
      }
    };

    syncOfflineTasks();
  }, []);

  // delete data and update database
  const deleteTask = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${id}`, {
        method: 'DELETE'
      });

      if(response.status === 204) {
        await fetchTasks();
        setMessage('Task deleted successfully.');
      } else {
        throw new Error('Failed to delete task');
      }
    } catch (error) {
      console.error(error);
      setErrorMessage('Failed to delete task. Please try again.');
    }
  };

  const handleEditTask = (id) => {
    setEditMode(id);
  }

  // update task in database
  const updateTaskName = async (id, newName) => {
    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${id}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          taskName: newName
        })
      });
  
      if (response.status === 200) {
        const updatedTask = await response.json();
        setTodoList(prevTodoList => prevTodoList.map(task => task.id === updatedTask.id ? updatedTask : task));
        setEditMode(null);
        setMessage('Task name updated successfully.');
      } else {
        throw new Error('Unable to edit TaskName');
      }
    } catch (error) {
      console.error(error);
      if (!isServerReachable()) {
        updateTemporaryTaskName(id, newName);
        setErrorMessage('Failed to update task name. Task will be updated offline.');
      } else {
        setErrorMessage('Failed to update task name. Please try again.');
      }
    }
  };
  
  const updateTemporaryTaskName = (id, newName) => {
    const updatedTasks = todoList.map(task => {
      if (task.id === id) {
        return { ...task, taskName: newName };
      }
      return task;
    });
  
    setTodoList(updatedTasks);
  
    const updatedOfflineTasks = updatedTasks.map(task => {
      if (task.id === id) {
        return { ...task, taskName: newName };
      }
      return task;
    });
    localStorage.setItem('offlineTasks', JSON.stringify(updatedOfflineTasks));
  
    setMessage('Task name updated offline. It will be synced when the network is available.');
  };
  

  // Function to close error message
  const closeErrorMessage = () => {
    setErrorMessage(null);
  };

  // Function to close messages
  const closeMessage = () => {
    setMessage(null);
    setErrorMessage(null);
  };

  return (
    <div className="App">
      {message && (
        <div className="message success">
          <p>{message}</p>
          <button onClick={closeMessage} className="close-message-button">Close</button>
        </div>
      )}

      {errorMessage && (
        <div className="error-message">
          <p>{errorMessage}</p>
          <button onClick={closeErrorMessage} className="close-error-button">Close</button>
        </div>
      )}

      <div className="addTask">
        <input value={newTask} onChange={handleChange}></input>
        <button onClick={addTask} className="addTask-button">
          Add Task
        </button>
      </div>
      <div className="list">
          <button onClick={toggleSortOrder}       className="toggle-sort-button">
            Toggle Sort Order
          </button>
        {
          todoList.map((task) => (
            <Task
              key={task.id}
              task={task}
              editMode={editMode === task.id}
              deleteTask={deleteTask}
              updateTaskName={updateTaskName}
              handleEditTask={handleEditTask}
              updateTemporaryTaskName={updateTemporaryTaskName}
            />
          ))
        }
      </div>
    </div>
  );
}

export default App;

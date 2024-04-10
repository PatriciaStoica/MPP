import './App.css';
import { useState, useEffect } from 'react';
import { Task } from './Task.js';

function App() {
  const [todoList, setTodoList] = useState([]);
  const [newTask, setNewTask] = useState("");
  const [editMode, setEditMode] = useState(null);
  const [message, setMessage] = useState(null);
  const [errorMessage, setErrorMessage] = useState(null);

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleChange = (event) => {
    setNewTask(event.target.value);
  }

  // fetch data from server
  const fetchTasks = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/tasks');
      if(!response.ok) {
        throw new Error('Failed to fetch tasks');
      }

      const data = await response.json();
      setTodoList(data);
    } catch (error) {
      console.error(error);
      setErrorMessage('Failed to fetch tasks. Please try again.');
    }
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
        body: JSON.stringify ({
          taskName: newTask,
          completed: false
        })
      });

      if (response.status === 201) {
        await fetchTasks();
        setNewTask('');
        setMessage('Task added successfully.');
      } else {
        throw new Error('Failed to add task');
      }
    } catch (error) {
      console.error(error);
      setErrorMessage('Failed to add task. Please try again.');
    }
  };

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

      if(response.status === 200) {
        await fetchTasks();
        setEditMode(null);
        setMessage('Task name updated successfully.');
      } else {
        throw new Error('Failed to update task name');
      }
    } catch (error) {
      console.error(error);
      setErrorMessage('Failed to update task name. Please try again.');
    }
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
        {
          todoList.map((task) => (
            <Task
              key={task.id}
              task={task}
              editMode={editMode === task.id}
              deleteTask={deleteTask}
              updateTaskName={updateTaskName}
              handleEditTask={handleEditTask}
            />
          ))
        }
      </div>
    </div>
  );
}

export default App;
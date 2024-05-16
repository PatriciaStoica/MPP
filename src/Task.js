import React, { useState, useEffect } from 'react';

export const Task = (props) => {
  const [editedTaskName, setEditedTaskName] = useState(props.task.taskName);
  const [selectedUserId, setSelectedUserId] = useState(props.task.userId);

  useEffect(() => {
    setEditedTaskName(props.task.taskName);
    setSelectedUserId(props.task.userId);
  }, [props.task.taskName, props.task.userId]);

  useEffect(() => {
    const storedSelectedUserId = localStorage.getItem(`selectedUser_${props.task.id}`);
    if (storedSelectedUserId) {
      setSelectedUserId(storedSelectedUserId);
    }
  }, [props.task.id]);

  const isTemporaryId = (id) => {
    return typeof id === 'string' && id.startsWith('temp_');
  }
  const handleEditChange = (event) => {
    setEditedTaskName(event.target.value);
  };

  const handleEditSubmit = () => {
    if (isTemporaryId(props.task.id)) {
      props.updateTemporaryTaskName(props.task.id, editedTaskName);
    } else {
      props.updateTaskName(props.task.id, editedTaskName);
    }

    props.handleEditTask(null);
  };

  const handleUserChange = async (event) => {
    const selectedUserId = event.target.value;
    setSelectedUserId(selectedUserId);
    
    try {
      const response = await fetch(`http://localhost:8080/api/tasks/${props.task.id}/user`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: selectedUserId
        })
      });
  
      if (response.status === 200) {
        // Task updated successfully
        props.setSelectedUser(props.task.id, selectedUserId); // Update selected user in parent component
      } else {
        // Handle error
        console.error('Failed to update task user');
      }
    } catch (error) {
      console.error(error);
      // Handle error
    }
  };

  return (
    <div className="task" style={{backgroundColor: props.task.completed ? "#C8E6C9" : "#fff"}}>
      {props.editMode ? (
        <input
          type="text"
          value={editedTaskName}
          onChange={handleEditChange}
        />
      ) : (
        <h1 className="taskName">{props.task.taskName}</h1>
      )}
      {props.editMode ? (
        <button className="editButton" onClick={handleEditSubmit}>Save</button>
      ) : (
        <button className="editButton" onClick={() => { console.log('Edit button clicked for task id:', props.task); props.handleEditTask(props.task.id)}}>Edit</button>
      )}
      <button className="deleteButton" onClick={() => props.deleteTask(props.task.id)}>X</button>
      <select value={selectedUserId} onChange={handleUserChange}>
        <option value="">Select User</option>
        {props.users.map(user => (
          <option key={user.id} value={user.id}>{user.userName}</option>
        ))}
      </select>
    </div>
  );
};
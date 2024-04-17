import React, { useState } from 'react';

export const Task = (props) => {
  const [editedTaskName, setEditedTaskName] = useState(props.taskName);

  const handleEditChange = (event) => {
    setEditedTaskName(event.target.value);
  };

  const handleEditSubmit = () => {
    props.updateTaskName(props.task.id, editedTaskName);
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
        <button className="editButton" onClick={() => props.handleEditTask(props.task.id)}>Edit</button>
      )}
      <button className="deleteButton" onClick={() => props.deleteTask(props.task.id)}>X</button>
    </div>
  );
};
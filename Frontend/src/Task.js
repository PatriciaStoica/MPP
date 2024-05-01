import React, { useState, useEffect } from 'react';

export const Task = (props) => {
  const [editedTaskName, setEditedTaskName] = useState(props.task.taskName);

  useEffect(() => {
    setEditedTaskName(props.task.taskName);
  }, [props.task.taskName]);

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
    </div>
  );
};

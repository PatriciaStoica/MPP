/*import com.example.MPPproject.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void getAllTasks_ReturnsListOfTasks() throws Exception {
        Task task = new Task(1L, "Task 1", false);
        List<Task> tasks = Arrays.asList(task);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskName", is("Task 1")));
    }

    @Test
    public void getTaskById_ExistingId_ReturnsTask() throws Exception {
        Task task = new Task(1L, "Task 1", false);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskName", is("Task 1")));
    }

    @Test
    public void getTaskById_NonexistentId_ReturnsNotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createTask_ValidTask_ReturnsCreated() throws Exception {
        Task task = new Task(1L, "Task 1", false);

        when(taskService.saveTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskName\": \"Task 1\", \"completed\": false}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskName", is("Task 1")));
    }

    @Test
    public void deleteTask_ExistingId_ReturnsNoContent() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteTask_NonexistentId_ReturnsNotFound() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateTask_ValidRequest_ReturnsOk() throws Exception {
        Task task = new Task(1L, "Updated Task Name", false);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(new Task(1L, "Task 1", false)));
        when(taskService.saveTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskName\": \"Updated Task Name\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskName", is("Updated Task Name")));
    }

    @Test
    public void updateTask_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskName\": \"\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}*/


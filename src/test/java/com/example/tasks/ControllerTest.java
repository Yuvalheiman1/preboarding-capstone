package com.example.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ResultActions creat_task_helper(String title, boolean done) throws Exception {
        return mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                    {"title": "%s", "done": %b}
                    """, title, done)));
    }

    private ResultActions update_task_helper(long id, String title, boolean done) throws Exception {
        return mockMvc.perform(put("/tasks/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                    {"title": "%s", "done": %b}
                    """, title, done)));
    }

    @Test
    void list_tasks_return_200() throws Exception {
        mockMvc.perform(get("/tasks")).andExpect(status().isOk());
    }

    @Test
    void list_tasks_return_200_and_singel_task() throws Exception {
        creat_task_helper("Task 1", true);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].done").value(true));
    }

    @Test
    void list_tasks_return_200_and_muliple_tasks() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", false);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                // First task
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].done").value(true))
                // Second task
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].done").value(false));
    }

    @Test
    void get_valid_task_returns_200_and_return_task() throws Exception {
        creat_task_helper("Task 1", true);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.done").value(true));

    }

    @Test
    void get_valid_task_returns_200_and_return_right_task() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", true);
        creat_task_helper("Task 3", true);

        mockMvc.perform(get("/tasks/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Task 2"))
                .andExpect(jsonPath("$.done").value(true));

    }

    @Test
    void get_missing_task_returns_404() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", true);
        creat_task_helper("Task 3", true);

        mockMvc.perform(get("/tasks/4"))
                .andExpect(status().isNotFound());

    }

    @Test
    void create_valid_task_returns_201_and_creat_task() throws Exception {
        creat_task_helper("Task 1", true)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void create_task_with_empty_title_returns_400() throws Exception {
        creat_task_helper("", true)
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_task_with_no_title_returns_400() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"done": true}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_task_with_no_done_returns_201_and_creat_task() throws Exception {
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"title": "Task 1"}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void delete_task_returns_404() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void delete_task_returns_204_and_delet_task() throws Exception {
        creat_task_helper("Task 1", true);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void update_existing_task_returns_200_and_updated_task() throws Exception {
        creat_task_helper("Task 1", false);

        update_task_helper(1, "Updated task", true)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void update_task_is_saved() throws Exception {
        creat_task_helper("Task 1", false);
        update_task_helper(1, "Updated task", true);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void update_missing_task_returns_404() throws Exception {
        update_task_helper(1, "Updated task", true)
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void update_task_with_empty_title_returns_400() throws Exception {
        creat_task_helper("Task 1", false);

        update_task_helper(1, "", true)
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_task_with_no_title_returns_400() throws Exception {
        creat_task_helper("Task 1", false);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"done": true}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_task_with_no_done_returns_400() throws Exception {
        creat_task_helper("Task 1", false);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"title": "Updated task"}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_task_does_not_change_other_tasks() throws Exception {
        creat_task_helper("Task 1", false);
        creat_task_helper("Task 2", false);

        update_task_helper(1, "Updated task", true);

        mockMvc.perform(get("/tasks/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 2"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void update_task_with_id_in_body_returns_400() throws Exception {
        creat_task_helper("Task 1", false);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"id": 99, "title": "Updated task", "done": true}
                    """))
                .andExpect(status().isBadRequest());

        // The rejected request must not change the task
        mockMvc.perform(get("/tasks/1"))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void update_task_with_non_numeric_id_returns_400() throws Exception {
        mockMvc.perform(put("/tasks/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"title": "Updated task", "done": true}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void list_tasks_filter_done_true_returns_only_done_tasks() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", false);
        creat_task_helper("Task 3", true);

        mockMvc.perform(get("/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Task 1", "Task 3")));
    }

    @Test
    void list_tasks_filter_done_false_returns_only_not_done_tasks() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", false);
        creat_task_helper("Task 3", false);

        mockMvc.perform(get("/tasks").param("done", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Task 2", "Task 3")));
    }

    @Test
    void list_tasks_filter_with_no_matches_returns_empty_list() throws Exception {
        creat_task_helper("Task 1", true);
        creat_task_helper("Task 2", true);

        mockMvc.perform(get("/tasks").param("done", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void list_tasks_filter_with_invalid_value_returns_400() throws Exception {
        mockMvc.perform(get("/tasks").param("done", "maybe"))
                .andExpect(status().isBadRequest());
    }
}

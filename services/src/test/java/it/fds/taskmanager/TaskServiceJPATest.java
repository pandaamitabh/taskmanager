package it.fds.taskmanager;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.fds.taskmanager.dto.TaskDTO;
import it.fds.taskmanager.repository.TasksRepository;

/**
 * Basic test suite to test the service layer, it uses an in-memory H2 database.
 * 
 * TODO Add more and meaningful tests! :)
 *
 * @author <a href="mailto:damiano@searchink.com">Damiano Giampaoli</a>
 * @since 10 Jan. 2018
 * @tester: Amitabh Panda
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TaskServiceJPATest extends Assert {

	@Autowired
	TaskService taskService;

	@Autowired
	private TasksRepository tasksRepo;

	@Test
	public void testWriteAndReadOnDB() {
		try {
			TaskDTO t = new TaskDTO();
			t.setTitle("Test task1");
			t.setStatus(TaskState.NEW.toString().toUpperCase());
			TaskDTO t1 = taskService.saveTask(t);
			TaskDTO tOut = taskService.findOne(t1.getUuid());
			assertEquals("Test task1", tOut.getTitle());
		} finally {
			tasksRepo.deleteAll();
		}
	}

	@Test
	public void testShowListWithPostponedTask() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setStatus(TaskState.POSTPONED.toString().toUpperCase());
		taskService.saveTask(t);
		List<TaskDTO> list = taskService.showList();
		assertEquals(0, list.size());
	}

	@Test
	public void testShowOneTask() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setDescription("Test Description");
		t.setPriority("0");
		t.setStatus(TaskState.NEW.toString().toUpperCase());
		TaskDTO t1 = taskService.saveTask(t);
		List<TaskDTO> list = taskService.showList();
		assertEquals(1, list.size());
		assertEquals("Test task1", t1.getTitle());
		assertEquals("Test Description", t1.getDescription());
		assertEquals("0", t1.getPriority());
		assertEquals(1, list.size());
	}

	// there can be multiple unit test cases written for individual "null"
	// assignment in the similar way

	@Test
	public void testSaveTaskBlank() {
		tasksRepo.deleteAll();
		TaskDTO task = new TaskDTO();
		task.setTitle("");
		task.setDescription("");
		task.setCreatedat(null);
		task.setDuedate(null);
		task.setPostponedat(null);
		task.setPostponedtime(null);
		task.setPriority("");
		task.setResolvedat(null);
		task.setStatus("");
		task.setUpdatedat(null);
		task.setUuid(null);

		boolean thrown = false;
		try {
			taskService.saveTask(task);
			// List<TaskDTO> showList = taskService.showList();
			// System.out.println(showList);
		} catch (Exception e) {
			thrown = true;
		}

		assertTrue(thrown);

	}

	@Test
	public void testResolveTask() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setStatus(TaskState.NEW.toString().toUpperCase());
		TaskDTO t1 = taskService.saveTask(t);
		taskService.resolveTask(t1.getUuid());
		TaskDTO tOut = taskService.findOne(t1.getUuid());
		assertEquals("RESOLVED", tOut.getStatus());

	}

	@Test
	public void testPostponeTask() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setStatus(TaskState.NEW.toString().toUpperCase());
		TaskDTO t1 = taskService.saveTask(t);
		taskService.postponeTask(t1.getUuid(), Calendar.MINUTE);
		TaskDTO tOut = taskService.findOne(t1.getUuid());
		assertEquals("POSTPONED", tOut.getStatus());
	}

	@Test
	public void testUnmarkPostponed() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setStatus(TaskState.POSTPONED.toString().toUpperCase());
		taskService.saveTask(t);
		// taskService.postponeTask(t1.getUuid(), Calendar.MINUTE);
		boolean thrown = false;
		try {
			taskService.unmarkPostoned();
		} catch (Exception e) {
			thrown = true;
			e.printStackTrace();
		}
		assertFalse(thrown);
		TaskDTO tOut = taskService.findOne(t.getUuid());
		assertEquals("RESTORED", tOut.getStatus());
	}

	@Test
	public void testSaveException() {
		tasksRepo.deleteAll();
		boolean thrown = false;
		try {
			taskService.saveTask(null);
		} catch (Exception e) {
			thrown = true;
			e.printStackTrace();
		}

		assertFalse(thrown);
	}

	@Test
	public void testFindOneThrowsException() {

		boolean thrown = false;
		try {
			taskService.findOne(UUID.randomUUID());
		} catch (Exception e) {
			thrown = true;
		}

		assertFalse(thrown);
	}

	@Test
	public void testFindOneNull() {

		boolean thrown = false;
		try {
			taskService.findOne(null);
		} catch (Exception e) {
			thrown = true;
		}

		assertFalse(thrown);
	}

	@Test
	public void testSaveTaskEmpty() {
		tasksRepo.deleteAll();
		TaskDTO task = new TaskDTO();
		task.setTitle("");
		task.setCreatedat(Calendar.getInstance());
		task.setDescription("");
		task.setStatus(TaskState.NEW.toString());
		task.setPostponedat(Calendar.getInstance());
		boolean thrown = false;
		try {
			taskService.saveTask(task);
		} catch (Exception e) {
			thrown = true;
		}
		assertNotEquals("null", task.getUpdatedat());
		assertFalse(thrown);

		// List<TaskDTO> showList = taskService.showList();
		// System.out.println(showList);
	}

	@Test
	public void testShowListEmpty() {
		tasksRepo.deleteAll();
		TaskDTO task = new TaskDTO();
		task.setCreatedat(Calendar.getInstance());
		task.setDescription("Test Description");
		task.setStatus(TaskState.NEW.toString());
		task.setPostponedat(Calendar.getInstance());
		boolean thrown = false;
		try {
			taskService.showList();
		} catch (Exception e) {
			thrown = true;
		}

		assertFalse(thrown);

	}

	@Test
	public void testUpdateTaskNull() {

		tasksRepo.deleteAll();
		boolean thrown = false;
		try {
			taskService.updateTask(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			thrown = true;
			e.printStackTrace();
		}
		assertFalse(thrown);
		tasksRepo.deleteAll();
	}

	@Test
	public void testUpdateTaskTitle() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setStatus(TaskState.NEW.toString().toUpperCase());
		TaskDTO t1 = taskService.saveTask(t);
		t1.setTitle("Test task2");
		TaskDTO t2 = taskService.updateTask(t1);
		TaskDTO tOut = taskService.findOne(t2.getUuid());
		assertEquals("Test task2", tOut.getTitle());
	}

	@Test
	public void testWriteAndReadOnDBEditOnFly() {
		tasksRepo.deleteAll();
		TaskDTO t = new TaskDTO();
		t.setTitle("Test task1");
		t.setTitle("Test task2");
		t.setStatus(TaskState.NEW.toString().toUpperCase());
		TaskDTO t1 = taskService.saveTask(t);
		TaskDTO tOut = taskService.findOne(t1.getUuid());
		assertEquals("Test task2", tOut.getTitle());
	}

	@EnableJpaRepositories
	@Configuration
	@SpringBootApplication
	public static class EndpointsMain {
	}
}

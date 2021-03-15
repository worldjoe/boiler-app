package net.singleclick.boilerapp;

import net.singleclick.boilerapp.repository.DocumentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import net.singleclick.boilerapp.models.*;
import net.singleclick.boilerapp.rest.services.*;

import java.time.LocalDateTime;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
public class BoilerAppApplicationTests {

	private static final String DOCUMENT_ID = "TEST ID";
	private static final String ALT_DOCUMENT_ID = "TEST ID 1";

	private static final LocalDateTime date = LocalDateTime.now();

	@InjectMocks
	private DocumentServiceImpl docService;

	@Mock
	private DocumentRepository documentRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void mockDatabase() {
		// when the database save is called, return the first argument back
		when(documentRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
	}

	private Document deepCopy(Document original)
	{
		Document doc = new Document();
		doc.setContent(original.getContent());
		doc.setCreatedDate(original.getCreatedDate());
		doc.setId(original.getId());
		doc.setLastModifiedDate(original.getLastModifiedDate());
		doc.setUserName(original.getUserName());
		return doc;
	}



	@Test
	public void testDocumentUpdate()
	{
		Document originalDoc = new Document();
		originalDoc.setContent("hi dan");
		originalDoc.setCreatedDate(LocalDateTime.now());
		originalDoc.setLastModifiedDate(LocalDateTime.now());
		originalDoc.setUserName("dano");
		originalDoc.setId(DOCUMENT_ID);

		try {
			docService.updateDocument(originalDoc, DOCUMENT_ID);
			fail("UPDATE non-existent document failed");
		}
		catch (Exception e) {
			assertTrue("UPDATE threw unexpected exception type", e instanceof DocumentNotFoundException);
		}

		Document createdDoc = docService.createDocument(originalDoc);
		assertNotNull(createdDoc);

		Document updatedDoc = deepCopy(originalDoc);
		updatedDoc.setContent("aloha Dan");
		updatedDoc.setLastModifiedDate(LocalDateTime.now());

		Document update = docService.updateDocument(updatedDoc, updatedDoc.getId());
		assertTrue(update.getContent().contains("aloha"));
	}

	@Test
	public void testInvalidDocumentId() {
		try {
			docService.updateDocument(new Document(), null);
			fail("UPDATE invalid documentId should fail");
		}
		catch (Exception e) {
			assertTrue("UPDATE threw unexpected exception type", e instanceof DocumentConstraintViolationException);
		}
	}

	@Test
	public void testNonMatchingDocumentIds() {
		try {
			Document originalDoc = new Document();
			originalDoc.setId(DOCUMENT_ID);
			docService.updateDocument(originalDoc, ALT_DOCUMENT_ID);
			fail("UPDATE invalid documentId should fail");
		}
		catch (Exception e) {
			assertTrue("UPDATE threw unexpected exception type", e instanceof DocumentConstraintViolationException);
		}
	}

	@Test
	public void testEmptyDocumentId()
	{
		Document originalDoc = new Document();
		originalDoc.setContent("hi dan");

		Document createdDoc = docService.createDocument(originalDoc);
		String documentId = createdDoc.getId();

		createdDoc.setContent("new content");
		createdDoc.setId(null);
		docService.updateDocument(createdDoc, documentId );
	}

	@Test
	public void testDocument()
	{
		Document originalDoc = new Document();
		originalDoc.setContent("hi dan");
		originalDoc.setCreatedDate(LocalDateTime.now());
		originalDoc.setLastModifiedDate(LocalDateTime.now());
		originalDoc.setUserName("dano");

		when(documentRepository.save(originalDoc)).thenReturn(originalDoc);

		Document createdDoc = docService.createDocument(originalDoc);
		String documentId = createdDoc.getId();

		Document readDoc =  docService.getDocument(documentId);
		assertEquals("GET failed", readDoc, createdDoc);

		readDoc.setContent("aloha dan");
		Document updatedDoc = docService.updateDocument(readDoc, documentId);
		assertEquals("Update failed", readDoc, updatedDoc);
		readDoc =  docService.getDocument(documentId);
		assertEquals("Second GET failed", readDoc, updatedDoc);

		docService.deleteDocument(documentId);
		try {
			readDoc = docService.getDocument(documentId);
			fail("GET non-existent document failed");
		}
		catch (Exception e) {
			assertTrue("GET threw unexpected exception type", e instanceof DocumentNotFoundException);
		}

	}
}
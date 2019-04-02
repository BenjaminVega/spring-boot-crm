package com.benjaminvega.crm.service;

import com.benjaminvega.crm.model.File;
import com.benjaminvega.crm.repository.FileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = FileService.class)
public class FileServiceTest {

    @InjectMocks
    private FileService cut;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileSystemService fileSystemService;

    private File expectedFile;

    @Before
    public void setUp() {
        expectedFile = File.builder().id(1L).name("customerProfile.png").build();
    }


    @Test
    public void addFirstPictureInTheSystem() {
        final ArgumentCaptor<File> argumentCaptor = ArgumentCaptor.forClass(File.class);
        when(fileRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.empty());
        when(fileRepository.save(any())).thenReturn(expectedFile);

        try {
            cut.addPicture(getPictureFromResourceFolder());
        } catch (Exception e) {
            e.printStackTrace();
            fail("test should not fail at this point");
        }

        verify(fileRepository).save(argumentCaptor.capture());
        assertEquals(expectedFile.getId(), argumentCaptor.getValue().getId());
        assertEquals(expectedFile.getName(), argumentCaptor.getValue().getName());

    }

    @Test
    public void addPictureOnTopOfOthersInTheSystem() {
        File expectedAddedFile = File.builder().id(2L).name("customerProfile.png").build();
        ArgumentCaptor<File> argumentCaptor = ArgumentCaptor.forClass(File.class);
        when(fileRepository.findFirstByOrderByIdDesc()).thenReturn(Optional.of(expectedFile));
        when(fileRepository.save(any())).thenReturn(expectedFile);

        try {
            cut.addPicture(getPictureFromResourceFolder());
        } catch (Exception e) {
            e.printStackTrace();
            fail("test should not fail at this point");
        }

        verify(fileRepository).save(argumentCaptor.capture());
        assertEquals(expectedAddedFile.getId(), argumentCaptor.getValue().getId());
        assertEquals(expectedAddedFile.getName(), argumentCaptor.getValue().getName());

    }

    @Test(expected = IOException.class)
    public void tryToUpdateAFileButItIsNotInFileSystem() throws IOException {
        when(fileRepository.findById(any())).thenReturn(Optional.of(expectedFile));
        doThrow(new IOException()).when(fileSystemService).move(any(), any(), any());

        cut.updatePicture(239L, 1873182L);

    }

    @Test
    public void tryToUpdateNonExistingPicture() throws IOException {
        when(fileRepository.findById(any())).thenReturn(Optional.empty());
        File file = cut.updatePicture(239L, 1873182L);
        assertNull(file);
    }

    @Test
    public void updatePicture() throws IOException {
        long customerId = 234L;
        File updateFile = File.builder()
                .id(expectedFile.getId())
                .name(expectedFile.getName())
                .path(expectedFile.getPath() + customerId + "/")
                .build();
        ArgumentCaptor<File> argumentCaptor = ArgumentCaptor.forClass(File.class);
        when(fileRepository.findById(any())).thenReturn(Optional.of(expectedFile));
        when(fileRepository.save(any())).thenReturn(updateFile);

        cut.updatePicture(239L, customerId);

        verify(fileRepository).save(argumentCaptor.capture());

        assertEquals(updateFile.getPath(), argumentCaptor.getValue().getPath());
    }

    @Test(expected = IOException.class)
    public void getPictureThatIsMissingInTheFileSystem() throws IOException {
        when(fileRepository.findById(any())).thenReturn(Optional.of(expectedFile));
        doThrow(new IOException()).when(fileSystemService).getFile(any());

        cut.getPictureById(34234L);
    }

    @Test
    public void tryToGetANonExistingPicture() throws IOException {
        when(fileRepository.findById(any())).thenReturn(Optional.empty());

        Resource resource = cut.getPictureById(34234L);

        assertNull(resource);
    }

    @Test
    public void getExistingPicture() throws IOException {
        when(fileRepository.findById(any())).thenReturn(Optional.of(expectedFile));
        when(fileSystemService.getFile(any())).thenReturn(mock(Resource.class));

        Resource resource = cut.getPictureById(34234L);

        assertNotNull(resource);
    }

    private MultipartFile getPictureFromResourceFolder() throws URISyntaxException {
        String name = "customerProfile.png";
        Path path = Paths.get(ClassLoader.getSystemResource(name).toURI());
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            fail(e.getMessage());
        }

        return new MockMultipartFile(name, name, "image/png", content);
    }
}

package lee.app;

import android.util.Pair;

import com.google.gson.Gson;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    public static final String StorageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=dreamstudy;"
            + "AccountKey=NV+ttpT3qnE4f2HoRuM03NIqcAeKj0M4kkZvqnvviEfRxDu+C5r12OcmJUgYKudwsyih3VQDuXPzJW8RTzUioA==";

    public static final String StorageMeetingContainer = "0224jisungmeeting";
    public static final String StoragePersonContainer = "0224jisungperson";
    public static final String StorageTutorsContainer = "0224jisungtutors";

    private Gson gson = new Gson();

    public Response<Person> getPerson(String id)  {
        try {
            CloudBlobContainer containerTutor = getTutorsContainer();
            CloudBlockBlob blobTutor = containerTutor.getBlockBlobReference(id);

            if (blobTutor.exists()) {
                Person person = gson.fromJson(blobTutor.downloadText(), Person.class);
                return new Response.Success(person);
            } else {
                CloudBlobContainer containerStudent = getPersonContainer();
                CloudBlockBlob blobStudent = containerStudent.getBlockBlobReference(id);

                if (blobStudent.exists()) {
                    Person person = gson.fromJson(blobStudent.downloadText(), Person.class);
                    return new Response.Success(person);
                } else {
                    return new Response.NotExist();
                }
            }
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<List<Person>> getTutors()  {
        List<Person> tutors = new ArrayList<>();
        try {
            CloudBlobContainer container = getTutorsContainer();
            Iterable<ListBlobItem> blobs = container.listBlobs();

            blobs.forEach(listBlobItem -> {
                if (listBlobItem instanceof CloudBlockBlob) {
                    Person tutor = null;
                    try {
                        tutor = gson.fromJson(((CloudBlockBlob)listBlobItem).downloadText(), Person.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tutors.add(tutor);
                }
            });
            return new Response.Success<>(tutors);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<Person> createPerson(Person person)  {
        CloudBlobContainer container;

        try {
            if (person.accountType.equals("Teacher") || person.accountType.equals("Admin")) {
                container = getTutorsContainer();
            } else {
                container = getPersonContainer();
            }

            CloudBlockBlob blob = container.getBlockBlobReference(person.id);

            if (blob.exists()) {
                return new Response.AlreadyExist();
            } else {
                blob.uploadText(gson.toJson(person));
                Session.person = person;
                return new Response.Success(person);
            }
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<Person> updatePerson(Person person)  {
        try {
            CloudBlobContainer container = getPersonContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(person.id);

            blob.uploadText(gson.toJson(person));
            Session.person = person;
            return new Response.Success(person);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> getStudySession(String date) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);

            if (blob.exists()) {
                StudySession studySession = gson.fromJson(blob.downloadText(), StudySession.class);
                return new Response.Success(studySession);
            } else {
                return new Response.NotExist();
            }
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> updateStudySession(String date, StudySession studySession) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);

            blob.uploadText(gson.toJson(studySession));
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> updateAttendee(String date, Person attendee) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);
            StudySession studySession;
            if (blob.exists()) {
                studySession = gson.fromJson(blob.downloadText(), StudySession.class);
                List<Person> existingAttendeeList = studySession.attendees;
                for (int i = 0; i < existingAttendeeList.size(); i++) {
                    Person existingAttendee = existingAttendeeList.get(i);
                    if (existingAttendee.id.equals(attendee.id)) {
                        return new Response.AlreadyExist();
                    }
                }

            } else {
                studySession = new StudySession();
            }
            studySession.attendees.add(attendee);
            blob.uploadText(gson.toJson(studySession));
            Session.person.matching.add(new Pair<String, Person>(date, null));
            updatePerson(Session.person);
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> removeAttendee(String date, Person attendee) {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);
            StudySession studySession;
            studySession = gson.fromJson(blob.downloadText(), StudySession.class);
            List<Person> existingAttendeeList = studySession.attendees;
            for (int i = 0; i < existingAttendeeList.size(); i++) {
                Person existingAttendee = existingAttendeeList.get(i);
                if (existingAttendee.id.equals(attendee.id)) {
                    existingAttendeeList.remove(i);
                    i--;
                }
            }
            blob.uploadText(gson.toJson(studySession));
            for (int i = 0; i < Session.person.matching.size(); i++) {
                if (Session.person.matching.get(i).first.equals(date)) {
                    Session.person.matching.remove(i);
                    i--;
                }
            }
            updatePerson(Session.person);
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    public Response<StudySession> updateMatchingResult(String date, List<Pair<Person, Person>> matching) throws URISyntaxException, InvalidKeyException, StorageException, IOException {
        try {
            CloudBlobContainer container = getStudySessionContainer();
            CloudBlockBlob blob = container.getBlockBlobReference(date);
            StudySession studySession = gson.fromJson(blob.downloadText(), StudySession.class);
            studySession.matching = matching;
            blob.uploadText(gson.toJson(studySession));
            return new Response.Success(studySession);
        } catch (Exception e) {
            return new Response.Failure();
        }
    }

    private CloudBlobContainer getPersonContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(StorageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        return blobClient.getContainerReference(StoragePersonContainer);
    }

    private CloudBlobContainer getStudySessionContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(StorageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        return blobClient.getContainerReference(StorageMeetingContainer);
    }

    private CloudBlobContainer getTutorsContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount account = CloudStorageAccount.parse(StorageConnectionString);
        CloudBlobClient blobClient = account.createCloudBlobClient();
        return blobClient.getContainerReference(StorageTutorsContainer);
    }
}


package lee.app

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = Repository()
    val login = MutableLiveData<ViewState>()
    val signUp = MutableLiveData<ViewState>()
    val updateAttendee = MutableLiveData<ViewState>()
    val attendees = MutableLiveData<StudySession>()
    val tutors = MutableLiveData<List<Person>>()
    var attendeesWithTutors = MediatorLiveData<StudySession>().apply {
        addSource(attendees) {
            if (tutors.value != null) {
                postValue(it)
            }
        }
        addSource(tutors) {
            if (attendees.value != null) {
                postValue(attendees.value)
            }
        }
    }

    fun login(id: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.getPerson(id)) {
                is Response.Success<Person> -> {
                    val data = response.data!!
                    if (data.password == password) {
                        Session.person = data
                        login.postValue(ViewState.SUCCESS)
                    } else login.postValue(ViewState.WRONG_PASSWORD)
                }
                is Response.NotExist -> login.postValue(ViewState.NOT_EXIST)
                else -> login.postValue(ViewState.FAILURE)
            }
        }
    }

    fun signUp(person: Person) {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.createPerson(person)) {
                is Response.Success<Person> -> signUp.postValue(ViewState.SUCCESS )
                is Response.AlreadyExist -> signUp.postValue(ViewState.ALREADY_EXIST)
                else -> signUp.postValue(ViewState.FAILURE)
            }
        }
    }

    fun updateAttendee(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.updateAttendee(date, Session.person)) {
                is Response.Success<StudySession> -> updateAttendee.postValue(ViewState.SUCCESS )
                is Response.AlreadyExist -> updateAttendee.postValue(ViewState.ALREADY_EXIST )
                else -> updateAttendee.postValue(ViewState.FAILURE)
            }
        }
    }

    fun removeAttendee(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.removeAttendee(date, Session.person)) {
                is Response.Success<StudySession> -> updateAttendee.postValue(ViewState.SUCCESS )
                else -> updateAttendee.postValue(ViewState.FAILURE)
            }
        }
    }

    fun getAttendee(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getStudySession(date)) {
                is Response.Success<StudySession> -> {
                    updateAttendee.postValue(ViewState.SUCCESS)
                    attendees.postValue(result.data)

                    if (result.data.attendees != null && result.data.attendees.isEmpty()) {
                        updateAttendee.postValue(ViewState.NOT_EXIST)
                    }
                }
                is Response.NotExist -> updateAttendee.postValue(ViewState.NOT_EXIST)
                else -> updateAttendee.postValue(ViewState.FAILURE)
            }
        }
    }

    fun loadTutors() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.tutors) {
                is Response.Success<List<Person>> -> {
                    tutors.postValue(result.data);
                }
            }
        }
    }

    fun updateMatchingForStudent(selectedStudent: Person, selectedTutor: Person?, selectedDate: String, studySession: StudySession) {
        viewModelScope.launch(Dispatchers.IO) {
            val pair = android.util.Pair(selectedDate, selectedTutor)
            for (i in 0 until selectedStudent.matching.size) {
                val date = selectedStudent.matching[i].first
                if (date == selectedDate) {
                    selectedStudent.matching.removeAt(i)
                    break
                }
            }
            selectedStudent.matching.add(pair)

            for (i in 0 until studySession.attendees.size) {
                if (studySession.attendees[i].id == selectedStudent.id) {
                    studySession.attendees.removeAt(i)
                    break
                }
            }
            studySession.attendees.add(selectedStudent)

            repository.updatePerson(selectedStudent)
            repository.updateStudySession(selectedDate, studySession)
            updateAttendee.postValue(ViewState.SUCCESS)
        }
    }

    enum class ViewState {
        SUCCESS,
        FAILURE,
        NOT_EXIST,
        ALREADY_EXIST,
        WRONG_PASSWORD,
    }
}
package lee.app

import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val repository = Repository()
    val login = MutableLiveData<ViewState>()
    val signUp = MutableLiveData<ViewState>()

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

    enum class ViewState {
        SUCCESS,
        FAILURE,
        NOT_EXIST,
        ALREADY_EXIST,
        WRONG_PASSWORD,
    }
}
package dhairya.pal.n01576099.dp.ui.dh1airya;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Dh1airyaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Dh1airyaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dh1airya fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
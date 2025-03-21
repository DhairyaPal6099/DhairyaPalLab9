import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dhairya.pal.n01576099.dp.R;
import dhairya.pal.n01576099.dp.databinding.FragmentDh1airyaBinding;

public class Dh1airya extends Fragment {

    private EditText editTxtCourseName;
    private EditText editTxtCourseDescription;
    private RecyclerView courseRV;
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;
    private FragmentDh1airyaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDh1airyaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        courseRV = binding.idRVCourses;
        editTxtCourseName = binding.idEdtCourseName;
        editTxtCourseDescription = binding.idEdtCourseDescription;
        Button buttonSave = binding.idBtnSave;
        Button buttonAdd = binding.idBtnAdd;
        Button buttonDelete = binding.idBtnDelete;

        loadData();
        buildRecyclerView();

        buttonAdd.setOnClickListener(view -> {
            courseModalArrayList.add(new CourseModal(editTxtCourseName.getText().toString(), editTxtCourseDescription.getText().toString()));
            adapter.notifyItemInserted(courseModalArrayList.size());
        });

        buttonSave.setOnClickListener(view -> saveData());
        buttonDelete.setOnClickListener(view -> deleteData());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.course_stuffxy), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(courseModalArrayList);
        editor.putString(getString(R.string.coursessss), json);
        editor.apply();
        Toast.makeText(getActivity(), getString(R.string.saved_array_list_to_shared_preferences), Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.course_stuffy), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.coursesx), null);
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();

        courseModalArrayList = gson.fromJson(json, type);

        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void buildRecyclerView() {
        adapter = new CourseAdapter(courseModalArrayList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        courseRV.setHasFixedSize(true);

        courseRV.setLayoutManager(manager);

        courseRV.setAdapter(adapter);
    }

    private void deleteData() {
        if (courseModalArrayList.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.no_data_to_delete), Toast.LENGTH_SHORT).show();
        } else {
            courseModalArrayList.clear();
            adapter.notifyItemRangeRemoved(0, courseModalArrayList.size());
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.course_stuff), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.courses));
        editor.apply();

        buildRecyclerView(); //To see the deleted changes real time

    }
}
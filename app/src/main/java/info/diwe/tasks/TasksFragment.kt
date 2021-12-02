package info.diwe.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import info.diwe.tasks.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = TaskViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // gives a lambda expression to Constructor TaskItemAdapter
        val adapter = TaskItemAdapter{taskId ->
            // when the User click on a Task, call TaskViewModel's onTaskClicked() method
            viewModel.onTaskClicked(taskId)
        }
        binding.rvTasksList.adapter = adapter

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer { taskId ->
            // the let block will only run if taskId is not null
            taskId?.let {
                val action = TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(taskId)
                this.findNavController().navigate(action)
                // sets the navigateToTask property to null
                viewModel.onTaskNavigated()
            }
        })

        // each time the tasks property gets a new value, TasksFragment submits its List<Task>
        // to the TaskItemAdapter
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
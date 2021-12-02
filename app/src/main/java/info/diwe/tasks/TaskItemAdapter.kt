package info.diwe.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.diwe.tasks.databinding.TaskItemBinding

// TaskItemAdapter uses TaskDiffItemCallback to compare its old data with the new.
// TaskItemAdapter accepts a lambda
class TaskItemAdapter(val clickListener: (taskId: Long) -> Unit):
        ListAdapter<Task, TaskItemAdapter.TaskItemViewHolder>(TaskDiffItemCallback()) {

    // onBindViewHolder is called each time the recycler view needs to display an item's data
    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val item = getItem(position)
        // calls TaskItemViewHolder bind method, passing it an item
        // pass the lambda to TaskItemVieHolder's bind() method
        holder.bind(item, clickListener)
    }

    // gets called when a view holder needs to be c reated
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder =
        TaskItemViewHolder.inflateFrom(parent)

    class TaskItemViewHolder(val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, clickListener: (taskId: Long) -> Unit) {
            // this sets the layout's data binding variable for the item
            binding.task = item
            // make the item respond to clicks
            binding.root.setOnClickListener {
                // execute the lambda when an item's clicked
                clickListener(item.taskId)
            }
        }

        companion object {
            fun inflateFrom(parent: ViewGroup): TaskItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                return TaskItemViewHolder(binding)
            }
        }
    }

}
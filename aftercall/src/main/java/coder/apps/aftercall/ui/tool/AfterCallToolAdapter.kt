package coder.apps.aftercall.ui.tool

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coder.apps.aftercall.AfterCallTool
import coder.apps.aftercall.databinding.PostCallItemToolBinding

class AfterCallToolAdapter(
    private val tools: List<AfterCallTool>,
    private val onClick: (AfterCallTool) -> Unit
) : RecyclerView.Adapter<AfterCallToolAdapter.ToolViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val binding = PostCallItemToolBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ToolViewHolder(binding)
    }

    override fun getItemCount(): Int = tools.size

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(tools[position])
    }

    inner class ToolViewHolder(
        private val binding: PostCallItemToolBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: AfterCallTool) {
            if (tool.iconRes != 0) binding.toolIcon.setImageResource(tool.iconRes)
            val ctx = binding.root.context
            binding.toolText.text = tool.label
                ?: if (tool.labelRes != 0) ctx.getString(tool.labelRes) else ""
            binding.toolRoot.setOnClickListener { onClick(tool) }
        }
    }
}

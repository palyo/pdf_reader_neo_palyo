package coder.apps.aftercall.ui.tab

import coder.apps.aftercall.R
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coder.apps.aftercall.ui.reply.QuickReplyAdapter
import coder.apps.aftercall.ui.reply.QuickReply
import coder.apps.aftercall.databinding.PostCallTabMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: PostCallTabMessagesBinding? = null
    private val binding get() = _binding!!

    private var adapter: QuickReplyAdapter? = null

    private val dataList = arrayListOf<QuickReply>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostCallTabMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = view.context
        dataList.clear()
        dataList.add(QuickReply("0", ctx.getString(R.string.quick_reply_1)))
        dataList.add(QuickReply("1", ctx.getString(R.string.quick_reply_2)))
        dataList.add(QuickReply("2", ctx.getString(R.string.quick_reply_3)))
        dataList.add(QuickReply("3", ctx.getString(R.string.quick_reply_4)))

        setupRecycler(ctx)
        setupInput(ctx)
    }

    private fun setupRecycler(ctx: android.content.Context) {
        binding.rvClipboard.layoutManager = LinearLayoutManager(ctx)

        adapter = QuickReplyAdapter(ctx, dataList) { position ->
            sendQuickMessageResult(dataList[position].name)
        }

        binding.rvClipboard.adapter = adapter
    }

    private fun setupInput(ctx: android.content.Context) {
        binding.icSend.visibility = if (binding.edtMsg.text.isNullOrEmpty()) View.GONE else View.VISIBLE

        binding.edtMsg.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                binding.icSend.visibility =
                    if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

                adapter?.let {
                    if (it.selectPos != -1) {
                        it.selectionClear()
                    }
                }
            }
        })

        binding.icSend.setOnClickListener {
            val text = binding.edtMsg.text.toString()
            if (text.isNotEmpty()) {
                sendQuickMessageResult(text)
            }
        }

        val accentColor = ContextCompat.getColor(ctx, R.color.colorAccent)

        binding.edtMsg.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivEdit.setColorFilter(accentColor, PorterDuff.Mode.SRC_IN)
                  adapter?.let {
                    if (it.selectPos != -1) {
                        it.selectionClear()
                    }
                }

            } else {
                binding.ivEdit.clearColorFilter()
            }
        }
    }

    private fun sendQuickMessageResult(text: String?) {
        parentFragmentManager.setFragmentResult(
            RESULT_KEY,
            bundleOf(RESULT_TEXT to text)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RESULT_KEY = "quick_message_result"
        const val RESULT_TEXT = "quick_message_text"
    }
}

package coder.apps.aftercall.ui.tab

import coder.apps.aftercall.R
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import coder.apps.aftercall.AfterCallConfig
import coder.apps.aftercall.AfterCallTool
import coder.apps.aftercall.extensions.EXTRA_MOBILE_NUMBER
import coder.apps.aftercall.databinding.PostCallTabDefaultBinding
import coder.apps.aftercall.ui.tool.AfterCallToolAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultMsgFragment : Fragment() {

    private var _binding: PostCallTabDefaultBinding? = null
    private val binding get() = _binding!!

    private var phoneAccountHandleList: List<PhoneAccountHandle>? = null
    private var strCallType: String? = null
    private var strName: String? = null
    private var strNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostCallTabDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneAccountHandleList = emptyList()
        initCallDetail()
        // The big primary-action tile (formerly populated by initPrimaryAction)
        // was removed from post_call_tab_default.xml — see the comment in that
        // layout. AfterCallConfig.primaryAction is preserved on the config
        // object for host apps that still want to set it, but it is no longer
        // rendered here.
        initToolGrid()
    }

    private fun initToolGrid() {
        val tools = AfterCallConfig.tools
        if (tools.isEmpty()) {
            binding.recyclerTools.visibility = View.GONE
            return
        }
        // 2 columns by default; 3 if there are 6+ tools so they stay compact
        val span = if (tools.size >= 6) 3 else 3
        binding.recyclerTools.visibility = View.VISIBLE
        binding.recyclerTools.layoutManager = GridLayoutManager(requireContext(), span)
        binding.recyclerTools.adapter = AfterCallToolAdapter(tools) { tool -> launchTool(tool) }
    }

    private fun launchTool(tool: AfterCallTool) {
        val act = activity ?: return
        try {
            val intent = tool.intentFactory(act)
            if (intent.flags == 0) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            act.startActivity(intent)
            act.finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initCallDetail() {
        activity?.apply {
            strNumber = intent.getStringExtra(EXTRA_MOBILE_NUMBER)

            if (!strNumber.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val contactName = withContext(Dispatchers.IO) {
                        getContactName(strNumber)
                    }
                    strName = contactName.ifEmpty { null }
                }
            }

            val stringExtra2 = intent.getStringExtra("state")
            strCallType = when (stringExtra2) {
                "3" -> getString(R.string.label_missed_call)
                "2" -> getString(R.string.label_call_end)
                else -> getString(R.string.label_call_end)
            }
        }
    }

    fun Context.getContactName(phoneNumber: String?): String {
        try {
            val queryUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            val cursor = contentResolver.query(queryUri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayName = it.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                    return displayName
                }
            }
            return getString(R.string.label_unknown)
        } catch (e: Exception) {
            return ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.hsd.avh.standstrong.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hsd.avh.standstrong.adapters.MessageAdapter
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.MessageViewModel

class MessageFragment : BaseFragment() {

    private lateinit var viewModel: MessageViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = com.hsd.avh.standstrong.databinding.FragmentMessageBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root
        //From the Post
        val motherId = MessageFragmentArgs.fromBundle(arguments).motherId
        val postId = MessageFragmentArgs.fromBundle(arguments).postId
        val factory = InjectorUtils.provideMessageViewModelFactory(context,motherId,postId)
        viewModel = ViewModelProviders.of(this, factory).get(MessageViewModel::class.java)

        val adapter = MessageAdapter()
        binding.messageList.adapter = adapter
        binding.messageList.addItemDecoration(DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL))
        binding.viewmodel = viewModel

        subscribeUi(adapter)


        setHasOptionsMenu(false)
        return binding.root
    }


    private fun subscribeUi(adapter: MessageAdapter) {
        viewModel.getMessages().observe(viewLifecycleOwner, Observer { msg->
            if (msg != null) adapter.submitList(msg)
        })

        /*viewModel.txtMessage.observe(viewLifecycleOwner, Observer { msg->
            if (msg != null) adapter.submitList(msg)
        })*/

    }
}

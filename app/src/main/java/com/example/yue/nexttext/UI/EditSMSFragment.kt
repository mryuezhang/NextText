package com.example.yue.nexttext.UI

import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import kotlinx.android.synthetic.main.fragment_eidt_sms.*

/**
 * Created by yue on 2017-11-01.
 */
class EditSMSFragment: Fragment() {
    private var messageDataPasser: MessageDataPasser? = null
    private var receivedMessage: MessageWrapper? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        messageDataPasser = context as MessageDataPasser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater!!.inflate(R.layout.fragment_eidt_sms, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        receivedMessage = arguments.getParcelable(Utilities.EDIT_DATA)

        edit_to.setText(receivedMessage!!.message._to, TextView.BufferType.EDITABLE)

        edit_content.setText(receivedMessage!!.message._content, TextView.BufferType.EDITABLE)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done_edit -> {
                if (edit_to.text.toString() == ""){
                    val alertDialog = AlertDialog.Builder(activity)
                            .setMessage("Recipient can't be empty")
                            .setPositiveButton("Yes") { d, _ -> d.cancel() }.create()

                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity.applicationContext,R.color.colorPrimary))
                    }

                    alertDialog.show()
                }
                else{
                    receivedMessage!!.message._to = edit_to.text.toString()

                    receivedMessage!!.message._content = edit_content.text.toString()

                    messageDataPasser!!.onDataPass(receivedMessage!!)
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.example.yue.nexttext.UI

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.yue.nexttext.R

/**
 * Created by yue on 2017-09-27.
 */
class MessageListAdapter(private val context: Context,
                         private val messageList: ArrayList<Message>): BaseAdapter() {

    private class ViewHolder{
        var title: TextView? = null
        var content: TextView? = null
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var viewHolder = ViewHolder()
        val view: View

        if(p1 == null){
            val layoutInflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.message_list_item_layout, p2, false)
            viewHolder.title = view.findViewById(R.id.textView_title)
            viewHolder.content = view.findViewById(R.id.textView_content)
            view.tag = viewHolder
        }else{
            view = p1
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.title?.text = this.messageList[p0].getTitle()
        viewHolder.content?.text = this.messageList[p0].getContent()

        return view
    }

    fun add(message: Message){
        this.messageList.add(message)
        this.notifyDataSetChanged()
    }

    fun removeAt(index: Int){
        this.messageList.removeAt(index)
        this.notifyDataSetChanged()
    }

    override fun getItem(p0: Int): Message = this.messageList[p0]

    override fun getItemId(p0: Int): Long = this.messageList[p0].hashCode().toLong()

    override fun getCount(): Int = this.messageList.size
}
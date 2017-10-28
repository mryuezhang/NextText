package com.example.yue.nexttext.UI

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R

/**
 * Created by yue on 2017-10-28.
 */
class MessageListAdapter(private val context: Context,
                         private var messageList: ArrayList<MessageWrapper>): BaseAdapter() {

    private var selectedItemsIDs = SparseBooleanArray()

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
            viewHolder.title = view.findViewById(R.id.messageList_item_title)
            viewHolder.content = view.findViewById(R.id.messageList_item_content)
            view.tag = viewHolder
        }else{
            view = p1
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.title?.text = this.messageList[p0].message._to
        viewHolder.content?.text = this.messageList[p0].message._content

        return view
    }

    override fun getItem(p0: Int): MessageWrapper = this.messageList[p0]

    override fun getItemId(p0: Int): Long = this.messageList[p0].hashCode().toLong()

    override fun getCount(): Int = this.messageList.size

    fun add(messageData: MessageWrapper){
        messageList.add(messageData)
        notifyDataSetChanged()
    }

    fun deleteAll(){
        messageList.clear()
        this.notifyDataSetChanged()
    }

    fun delete(item : MessageWrapper){
        messageList.remove(item)
        this.notifyDataSetChanged()
    }

    fun toggleSelection(position: Int) {
        selectView(position, !selectedItemsIDs.get(position))
    }

    private fun selectView(i: Int, boo: Boolean){
        if(boo) selectedItemsIDs.put(i, boo)
        else selectedItemsIDs.delete(i)
    }

    fun getSelectedIds(): SparseBooleanArray = selectedItemsIDs

    fun removeSelection() {
        selectedItemsIDs = SparseBooleanArray()
        notifyDataSetChanged()
    }

    fun setMessageList(newList: ArrayList<MessageWrapper>) {
        this.messageList = newList
        notifyDataSetChanged()
    }
}
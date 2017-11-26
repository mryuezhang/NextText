package com.example.yue.nexttext.UI

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.yue.nexttext.DataType.MessageWrapper
import com.example.yue.nexttext.R
import java.util.*


/**
 * Created by yue on 2017-10-28.
 */
class MessageListAdapter(private val activity:Activity,
                         private var messageList: ArrayList<MessageWrapper>): BaseAdapter(), Filterable {
    private var selectedItemsIDs = SparseBooleanArray()
    private var fullList = messageList
    private var queryText: String = ""

    private class ViewHolder{
        var title: TextView? = null
        var content: TextView? = null
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var viewHolder = ViewHolder()
        val view: View

        if(p1 == null){
            val layoutInflater: LayoutInflater = this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.message_list_item_layout, p2, false)
            viewHolder.title = view.findViewById(R.id.messageList_item_title)
            viewHolder.content = view.findViewById(R.id.messageList_item_content)
            view.tag = viewHolder
        }
        else{
            view = p1
            viewHolder = view.tag as ViewHolder
        }

        val messageTitle = this.messageList[p0].message._to

        val messageContent = if (messageTitle.contains("@")) {
            val emailSubject = if (this.messageList[p0].message._subject == null || this.messageList[p0].message._subject == ""){
                "(no subject)"
            } else {
                this.messageList[p0].message._subject
            }
            emailSubject + "\n" + this.messageList[p0].message._content
        } else {
            this.messageList[p0].message._content
        }

        var startPosition: Int = messageTitle.toLowerCase(Locale.getDefault()).trim().indexOf(queryText)
        var endPosition: Int = startPosition + queryText.length
        if (startPosition > -1){
            val spannable = SpannableString(messageTitle)
            val colorAccent = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.argb(255,255,64,129)))
            val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, colorAccent, null)
            spannable.setSpan(highlightSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewHolder.title?.text = spannable
        }
        else viewHolder.title?.text = messageTitle

        startPosition= messageContent.toLowerCase(Locale.getDefault()).trim().indexOf(queryText)
        endPosition= startPosition + queryText.length

        if (startPosition > -1){
            val spannable = SpannableString(messageContent)
            val colorAccent = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.argb(255,255,64,129)))
            val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, colorAccent, null)
            spannable.setSpan(highlightSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewHolder.content?.text = spannable
        }
        else viewHolder.content?.text = messageContent

        //viewHolder.title?.text = this.messageList[p0].message._to
        //viewHolder.content?.text = this.messageList[p0].message._content

        return view
    }

    override fun getItem(p0: Int): MessageWrapper = this.messageList[p0]

    override fun getItemId(p0: Int): Long = this.messageList[p0].hashCode().toLong()

    override fun getCount(): Int = this.messageList.size

    fun add(messageData: MessageWrapper){
        this.messageList.add(0, messageData)
        notifyDataSetChanged()
    }

    fun replace(messageData: MessageWrapper){
        for ((i, item) in messageList.withIndex()){
            if (item.id == messageData.id) messageList[i] = messageData
        }
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
        this.fullList = messageList
        notifyDataSetChanged()
    }

    private inner class MessageFilter: Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            messageList = fullList
            val filterResult = FilterResults()
            if(p0 != null){
                val definedQuery = p0.toString().toLowerCase(Locale.getDefault()).trim()
                queryText = definedQuery
                if (definedQuery == "") {
                    filterResult.count = messageList.size
                    filterResult.values = messageList
                } else {
                    val tempList = ArrayList<MessageWrapper>()
                    for (messageWrapper in messageList) {
                        if (messageWrapper.message._to.toLowerCase(Locale.getDefault()).contains(definedQuery) ||
                                messageWrapper.message._content.toLowerCase(Locale.getDefault()).contains(definedQuery)) tempList.add(messageWrapper)
                    }
                    filterResult.count = tempList.size
                    filterResult.values = tempList
                }
            }
            return filterResult
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            val filteredList = p1?.values as ArrayList<MessageWrapper>

            if(p0 != null){
                val definedQuery = p0.toString().toLowerCase(Locale.getDefault()).trim()
                if (filteredList.size == 0) {
                    if(definedQuery == "") activity.findViewById<TextView>(R.id.no_result_text).text = "No result"
                    else {
                        val string = "No result found for \'<b>" + p0.toString() + "</b>\'"
                        activity.findViewById<TextView>(R.id.no_result_text).text = Html.fromHtml(string, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                    }
                }
                messageList = if (definedQuery != "") filteredList
                else fullList
            }
            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter = MessageFilter()
}
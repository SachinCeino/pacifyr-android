package com.pacifyr.client.quickblox.chat.ui.adapter;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.pacifyr.client.customview.MainTextView;
import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.R;
import com.pacifyr.client.quickblox.chat.utils.qb.QbDialogUtils;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;

import com.quickblox.sample.core.ui.adapter.BaseSelectableListAdapter;
import com.quickblox.sample.core.utils.ResourceUtils;
import com.quickblox.sample.core.utils.UiUtils;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.pacifyr.client.network.NetworkService.GLOBAL_IMAGE_URL;
import static com.pacifyr.client.utils.Utility.isValid;

public class DialogsAdapter extends BaseSelectableListAdapter<QBChatDialog> {

    private static final String EMPTY_STRING = "";

    public DialogsAdapter(Context context, List<QBChatDialog> dialogs) {
        super(context, dialogs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_list_item_dialog, parent, false);

            holder = new ViewHolder();
            holder.rootLayout = (ViewGroup) convertView.findViewById(R.id.root);
            holder.nameTextView = (MainTextView) convertView.findViewById(R.id.text_dialog_name);
            holder.lastMessageTextView = (MainTextView) convertView.findViewById(R.id.text_dialog_last_message);
            holder.dialogImageView = (CircleImageView) convertView.findViewById(R.id.image_dialog_icon);
            holder.unreadCounterTextView = (MainTextView) convertView.findViewById(R.id.text_dialog_unread_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QBChatDialog dialog = getItem(position);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
            holder.dialogImageView.setImageResource(R.drawable.ic_chat_group);
        } else {
            holder.dialogImageView.setBackgroundDrawable(UiUtils.getGreyCircleDrawable());
//           holder.dialogImageView.setImageResource(R.drawable.placeholder_user);

 /*           String imgUrl = QbDialogUtils.getDialogImage(dialog);
            if (imgUrl==null)
                Picasso.with(context).load(GLOBAL_IMAGE_URL+imgUrl).placeholder(R.drawable.placeholder_user).into( holder.dialogImageView);
            else
                Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).into( holder.dialogImageView);
*/
       /*     if(isValid(pacifyerDetial.getImageUrl())) {
                imgUrl = pacifyerDetial.getImageUrl();
                if (isValidhtml(imgUrl))
                    Picasso.with(this).load(imgUrl).placeholder(R.drawable.placeholder_user).into(profileCimv);
            }
*/

        /*    holder.dialogImageView.setBackgroundDrawable(UiUtils.getColorCircleDrawable(position));
            holder.dialogImageView.setImageDrawable(null);*/
        }
       QBUser user= QbDialogUtils.getOpposteUser(dialog,context);
        if(isValid(user))
        {
            if (isValid(user.getFullName()))
            {
                String res = "";
                try {
                    for(int i = 0; i < user.getFullName().length(); i++) {
                        Character ch = user.getFullName().charAt(i);
                        if(Character.isUpperCase(ch))
                            res += " " +ch;
                        else
                            res += ch;
                    }
                }catch (Exception e)
                {

                }
                if (isValid(res))
                {
                    holder.nameTextView.setText(res);
                }
                else
                {
                    holder.nameTextView.setText(user.getFullName());
                }
            }




            String imgUrl =  TextUtils.isEmpty(user.getCustomData()) ? user.getLogin() : user.getCustomData();
            if (imgUrl == null)
                Picasso.with(context).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);
            else
                Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);
        }

      /*  if(isPacifyer()) {

            holder.nameTextView.setText(QbDialogUtils.getOpposteUserName(dialog,context));

            String imgUrl = QbDialogUtils.getDialogImage(dialog);
            if (imgUrl == null)
                Picasso.with(context).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);
            else
                Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);
        }else {

            if (isCustomer()) {
                holder.nameTextView.setText(QbDialogUtils.getOpposteUserName(dialog,context));

                String imgUrl = QbDialogUtils.getDialogImage(dialog);
                if (imgUrl == null)
                    Picasso.with(context).load(GLOBAL_IMAGE_URL + imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);
                else
                    Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).into(holder.dialogImageView);


            }

        }*/



        holder.lastMessageTextView.setText(prepareTextLastMessage(dialog));

        int unreadMessagesCount = getUnreadMsgCount(dialog);
        if (unreadMessagesCount == 0) {
            holder.unreadCounterTextView.setVisibility(View.GONE);
        } else {
            holder.unreadCounterTextView.setVisibility(View.VISIBLE);
            holder.unreadCounterTextView.setText(String.valueOf(unreadMessagesCount > 99 ? 99 : unreadMessagesCount));
        }


        holder.rootLayout.setBackgroundColor(isItemSelected(position) ? ResourceUtils.getColor(android.R.color.transparent) :
                ResourceUtils.getColor(android.R.color.transparent));

      /*  holder.rootLayout.setBackgroundColor(isItemSelected(position) ? ResourceUtils.getColor(R.color.selected_list_item_color) :
                ResourceUtils.getColor(android.R.color.transparent));*/

        return convertView;
    }

    private int getUnreadMsgCount(QBChatDialog chatDialog){
        Integer unreadMessageCount = chatDialog.getUnreadMessageCount();
        if (unreadMessageCount == null) {
            return 0;
        } else {
            return unreadMessageCount;
        }
    }

    private boolean isLastMessageAttachment(QBChatDialog dialog) {
        String lastMessage = dialog.getLastMessage();

        Integer lastMessageSenderId = dialog.getLastMessageUserId();
        return TextUtils.isEmpty(lastMessage) && lastMessageSenderId != null;
    }

    private String prepareTextLastMessage(QBChatDialog chatDialog){
        if (isLastMessageAttachment(chatDialog)){
            return context.getString(R.string.chat_attachment);
        } else {
            return chatDialog.getLastMessage();
        }
    }

    private static class ViewHolder {
        ViewGroup rootLayout;
        CircleImageView dialogImageView;
        MainTextView nameTextView;
        MainTextView lastMessageTextView;
        MainTextView unreadCounterTextView;
    }


    public boolean isPacifyer()
    {
        boolean isPacifyer=false;
        try{

            try {

                String label=getUserType();

                if(label.equalsIgnoreCase("pacifyr"))
                    isPacifyer=true;
                else if(label.equals("customer")) {
                    isPacifyer=false;
                }
            } catch (Exception t) {
                isPacifyer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isPacifyer=false;
        }
        return isPacifyer;
    }
    public boolean isCustomer()
    {
        boolean isCustomer=false;
        try{

            try {

                String label=getUserType();
                if(label.equals("customer")) {
                    isCustomer=true;
                }
                else if(label.equalsIgnoreCase("pacifyr"))
                    isCustomer=false;

            } catch (Exception t) {
                isCustomer=false;
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            isCustomer=false;
        }
        return isCustomer;
    }

    public String  getUserType()
    {
        String label="customer";
        try{

            try {

                label= PrefManager.getInstance(context).getUserType();


            } catch (Exception t) {
                //  LogUtils.LOGD("session","SSA getMSessionRating list exce " + t.getMessage());
            }
        }catch (Exception e)
        {
            label="customer";
        }
        return label;
    }

}

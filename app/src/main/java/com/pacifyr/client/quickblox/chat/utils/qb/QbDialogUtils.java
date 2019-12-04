package com.pacifyr.client.quickblox.chat.utils.qb;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pacifyrapp.utilitylibrary.utils.PrefManager;
import com.pacifyr.client.network.NetworkService;
import com.pacifyr.client.quickblox.chat.utils.chat.ChatHelper;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;


import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.pacifyr.client.utils.Utility.isValid;

public class QbDialogUtils {
    private static final String TAG = QbDialogUtils.class.getSimpleName();



    public static QBChatDialog createDialog(List<QBUser> users) {
        QBUser currentUser = ChatHelper.getCurrentUser();
        users.remove(currentUser);

        return DialogUtils.buildDialog(users.toArray(new QBUser[users.size()]));
    }

    public static List<QBUser> getAddedUsers(QBChatDialog dialog, List<QBUser> currentUsers) {
        return getAddedUsers(getQbUsersFromQbDialog(dialog), currentUsers);
    }

    public static List<QBUser> getAddedUsers(List<QBUser> previousUsers, List<QBUser> currentUsers) {
        List<QBUser> addedUsers = new ArrayList<>();
        for (QBUser currentUser : currentUsers) {
            boolean wasInChatBefore = false;
            for (QBUser previousUser : previousUsers) {
                if (currentUser.getId().equals(previousUser.getId())) {
                    wasInChatBefore = true;
                    break;
                }
            }
            if (!wasInChatBefore) {
                addedUsers.add(currentUser);
            }
        }

        QBUser currentUser = ChatHelper.getCurrentUser();
        addedUsers.remove(currentUser);

        return addedUsers;
    }

    public static List<QBUser> getRemovedUsers(QBChatDialog dialog, List<QBUser> currentUsers) {
        return getRemovedUsers(getQbUsersFromQbDialog(dialog), currentUsers);
    }

    public static List<QBUser> getRemovedUsers(List<QBUser> previousUsers, List<QBUser> currentUsers) {
        List<QBUser> removedUsers = new ArrayList<>();
        for (QBUser previousUser : previousUsers) {
            boolean isUserStillPresented = false;
            for (QBUser currentUser : currentUsers) {
                if (previousUser.getId().equals(currentUser.getId())) {
                    isUserStillPresented = true;
                    break;
                }
            }
            if (!isUserStillPresented) {
                removedUsers.add(previousUser);
            }
        }

        QBUser currentUser = ChatHelper.getCurrentUser();
        removedUsers.remove(currentUser);

        return removedUsers;
    }

    public static void logDialogUsers(QBChatDialog qbDialog) {
        Log.v(TAG, "Dialog " + getDialogName(qbDialog));
        logUsersByIds(qbDialog.getOccupants());
    }

    public static void logUsers(List<QBUser> users) {
        for (QBUser user : users) {
            Log.i(TAG, user.getId() + " " + user.getFullName());
        }
    }

    private static void logUsersByIds(List<Integer> users) {
        for (Integer id : users) {
            QBUser user = QbUsersHolder.getInstance().getUserById(id);
            Log.i(TAG, user.getId() + " " + user.getFullName());
        }
    }

    public static Integer[] getUserIds(List<QBUser> users) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (QBUser user : users) {
            ids.add(user.getId());
        }
        return ids.toArray(new Integer[ids.size()]);
    }

    public static String getDialogName(QBChatDialog dialog) {
        Log.d(TAG, "c1 dialog: "+dialog);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getName();
        } else {
            // It's a private dialog, let's use opponent's name as chat name
            Integer opponentId = dialog.getRecipientId();
            Log.d(TAG, "c21 dialog.getUserId(): "+dialog.getUserId());
            Log.d(TAG, "c22 dialog.getDialogId(): "+dialog.getDialogId());
            Log.d(TAG, "c23 dialog.getRecipientId(): "+dialog.getRecipientId());
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);
/*
            Integer opponentId1 = dialog.getUserId();
            QBUser user1 = QbUsersHolder.getInstance().getUserById(opponentId1);
            Log.d(TAG, "c25 user1.getFullName(): "+user1.getFullName());*/

            if (user != null) {
                Log.d(TAG, "c24 user.getFullName(): "+user.getFullName());

                return TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
            } else {
                Log.d(TAG, "c2 dialog.getName(): "+dialog.getName());
                return dialog.getName();
            }
        }
    }
    public static String getOpposteUserName(QBChatDialog dialog,Context context) {
        Log.d(TAG, "p1 dialog: "+dialog);
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getName();
        } else {

            Integer opponentId = dialog.getRecipientId();
            Log.d(TAG, "p21 dialog.getUserId(): " + dialog.getUserId());
            Log.d(TAG, "p22 dialog.getDialogId(): " + dialog.getDialogId());
            Log.d(TAG, "p23 dialog.getRecipientId(): " + dialog.getRecipientId());
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);


            String qbid=getQbid(context);
            if(isValid(qbid))
            {
                if(dialog.getRecipientId()!=Integer.parseInt(qbid))
                    user = QbUsersHolder.getInstance().getUserById(dialog.getRecipientId());
                else
                    user=QbUsersHolder.getInstance().getUserById(dialog.getUserId());

            }

            if (user != null) {
                Log.d(TAG, "p24 user.getFullName(): "+user.getFullName());

                return TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
            } else {
                Log.d(TAG, "p2 dialog.getName(): "+dialog.getName());

                return dialog.getName();
            }
        }
    }
    public static QBUser getOpposteUser(QBChatDialog dialog,Context context) {
        Log.d(TAG, "p1 dialog: "+dialog);
      /*  if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getName();
        } else {*/

            Integer opponentId = dialog.getRecipientId();
            Log.d(TAG, "p21 dialog.getUserId(): " + dialog.getUserId());
            Log.d(TAG, "p22 dialog.getDialogId(): " + dialog.getDialogId());
            Log.d(TAG, "p23 dialog.getRecipientId(): " + dialog.getRecipientId());
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);


            String qbid=getQbid(context);

        Log.d(TAG, "p33 qbid: " + qbid);
            if(isValid(qbid)) {
                if (dialog.getRecipientId() != Integer.parseInt(qbid)) {
                    user = QbUsersHolder.getInstance().getUserById(dialog.getRecipientId());
                    return user;
                } else {
                    user = QbUsersHolder.getInstance().getUserById(dialog.getUserId());
                    return user;
                }

            }


            return user;
      //  }
    }
    private static String getQbid(Context context) {
        String id=null;
        try{
            if(isValid(NetworkService.mCurrentUser))
                id= NetworkService.mCurrentUser.getQbid()+"";
            else
                id=PrefManager.getInstance(context).getQBID();

        }catch (Exception e)
        {
           id=null;
        }
        return id;
    }

    public static String getDialogImage(QBChatDialog dialog) {

        if (dialog.getType().equals(QBDialogType.GROUP)) {


            return dialog.getPhoto();
        } else {


            // It's a private dialog, let's use opponent's name as chat name
            Integer opponentId = dialog.getRecipientId();
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);
            if (user != null) {
                return TextUtils.isEmpty(user.getCustomData()) ? user.getLogin() : user.getCustomData();
            } else {
                return dialog.getPhoto();
            }
        }
    }

    public static String getDialogImageChat(QBChatDialog dialog,Context context) {

        Integer opponentId = dialog.getRecipientId();
        Log.d(TAG, "p21 dialog.getUserId(): " + dialog.getUserId());
        Log.d(TAG, "p22 dialog.getDialogId(): " + dialog.getDialogId());
        Log.d(TAG, "p23 dialog.getRecipientId(): " + dialog.getRecipientId());
        QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);


        String qbid=getQbid(context);
        if(isValid(qbid)) {
            if (dialog.getRecipientId() != Integer.parseInt(qbid)) {
                user = QbUsersHolder.getInstance().getUserById(dialog.getRecipientId());
                return TextUtils.isEmpty(user.getCustomData()) ? user.getLogin() : user.getCustomData();
            } else {
                user = QbUsersHolder.getInstance().getUserById(dialog.getUserId());
                return TextUtils.isEmpty(user.getCustomData()) ? user.getLogin() : user.getCustomData();
            }

        }
        return  user.getCustomData();

    }

    public static String getDialogImagepacifyr(QBChatDialog dialog) {

        if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getPhoto();
        } else {
            // It's a private dialog, let's use opponent's name as chat name
            Integer opponentId = dialog.getUserId();
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);
            if (user != null) {
                return TextUtils.isEmpty(user.getCustomData()) ? user.getLogin() : user.getCustomData();
            } else {
                return dialog.getPhoto();
            }
        }
    }


    private static List<QBUser> getQbUsersFromQbDialog(QBChatDialog dialog) {
        List<QBUser> previousDialogUsers = new ArrayList<>();
        for (Integer id : dialog.getOccupants()) {
            QBUser user = QbUsersHolder.getInstance().getUserById(id);
            if (user == null) {
                throw new RuntimeException("User from dialog is not in memory. This should never happen, or we are screwed");
            }
            previousDialogUsers.add(user);
        }
        return previousDialogUsers;
    }

    public static List<Integer> getOccupantsIdsListFromString(String occupantIds) {
        List<Integer> occupantIdsList = new ArrayList<>();
        String[] occupantIdsArray = occupantIds.split(",");
        for (String occupantId : occupantIdsArray) {
            occupantIdsList.add(Integer.valueOf(occupantId));
        }
        return occupantIdsList;
    }

    public static String getOccupantsIdsStringFromList(Collection<Integer> occupantIdsList) {
        return TextUtils.join(",", occupantIdsList);
    }

    public static QBChatDialog buildPrivateChatDialog(String dialogId, Integer recipientId){
        QBChatDialog chatDialog = DialogUtils.buildPrivateDialog(recipientId);
        chatDialog.setDialogId(dialogId);

        return chatDialog;
    }
}

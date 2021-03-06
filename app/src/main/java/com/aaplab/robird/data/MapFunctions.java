package com.aaplab.robird.data;

import android.database.Cursor;

import com.aaplab.robird.data.entity.Account;
import com.aaplab.robird.data.entity.Contact;
import com.aaplab.robird.data.entity.Direct;
import com.aaplab.robird.data.entity.Tweet;
import com.aaplab.robird.data.entity.UserList;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;
import twitter4j.Status;

/**
 * Created by majid on 16.01.15.
 */
public final class MapFunctions {
    public static Func1<SqlBriteContentProvider.Query, Account> ACCOUNT = new Func1<SqlBriteContentProvider.Query, Account>() {
        @Override
        public Account call(SqlBriteContentProvider.Query query) {
            Cursor cursor = query.run();
            Account account = null;

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                account = Account.from(cursor);
            }

            cursor.close();
            return account;
        }
    };

    public static Func1<SqlBriteContentProvider.Query, List<Account>> ACCOUNT_LIST = new Func1<SqlBriteContentProvider.Query, List<Account>>() {
        @Override
        public List<Account> call(SqlBriteContentProvider.Query query) {
            ArrayList<Account> accounts = new ArrayList<>();
            Cursor cursor = query.run();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    accounts.add(Account.from(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return accounts;
        }
    };

    public static Func1<SqlBriteContentProvider.Query, List<Tweet>> TWEET_LIST = new Func1<SqlBriteContentProvider.Query, List<Tweet>>() {
        @Override
        public List<Tweet> call(SqlBriteContentProvider.Query query) {
            List<Tweet> tweets = new ArrayList<>();
            Cursor cursor = query.run();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    tweets.add(Tweet.from(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return tweets;
        }
    };

    public static Func1<SqlBriteContentProvider.Query, List<Direct>> DIRECTS = new Func1<SqlBriteContentProvider.Query, List<Direct>>() {
        @Override
        public List<Direct> call(SqlBriteContentProvider.Query query) {
            List<Direct> directs = new ArrayList<>();
            Cursor cursor = query.run();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    directs.add(Direct.from(cursor));
                } while (cursor.moveToNext());

            }

            cursor.close();
            return directs;
        }
    };

    public static Func1<SqlBriteContentProvider.Query, List<UserList>> USER_LISTS = new Func1<SqlBriteContentProvider.Query, List<UserList>>() {
        @Override
        public List<UserList> call(SqlBriteContentProvider.Query query) {
            List<UserList> lists = new ArrayList<>();
            Cursor cursor = query.run();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    lists.add(UserList.from(cursor));
                } while (cursor.moveToNext());

            }

            cursor.close();
            return lists;
        }
    };

    public static Func1<SqlBriteContentProvider.Query, List<Contact>> CONTACT_LIST = new Func1<SqlBriteContentProvider.Query, List<Contact>>() {
        @Override
        public List<Contact> call(SqlBriteContentProvider.Query query) {
            List<Contact> contacts = new ArrayList<>();
            Cursor cursor = query.run();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    contacts.add(Contact.from(cursor));
                } while (cursor.moveToNext());

            }

            cursor.close();
            return contacts;
        }
    };

    public static Func1<List<Status>, List<Tweet>> STATUSES_TO_TWEETS = new Func1<List<Status>, List<Tweet>>() {
        @Override
        public List<Tweet> call(List<Status> statuses) {
            ArrayList<Tweet> tweets = new ArrayList<>(statuses.size());
            for (Status status : statuses)
                tweets.add(Tweet.from(status));

            return tweets;
        }
    };
}

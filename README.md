# learn_Notification
这是一个学习Notification的Demo。项目地址：[Notification学习](https://github.com/crazymen-nanke/learn_Notification)



通知（Notiﬁcation）是Android系统提供的一种具有全局效果的通知功能，可以在系统通知栏中 显示。当APP向系统发出通知时它将先以图标的形式显示在通知栏中。用户可以下拉通知栏查看通 知的详细信息。通知栏和抽屉式通知栏均是由系统控制，用户可以随时查看。 想要了解通知的基本概念我们先来看一下通知的使用方法。通知的创建比较灵活，可以在活动中创 建也可以在广播接收器中创建，当然也可以在我们上一章学习的服务中创建，相比后两种创建方 式，在活动中创建通知的情况比较少见，一般只有当程序进入后台执行时才会需要这种类型的通 知。 首先我们需要通过调用***Context***的***getSystemService()***方法获取***NotiﬁcationManager***来进行通知的 管理，在***getSystemService()***方法中接收一个字符串参数来确定获取系统的哪个服务。通常我们传 入***Context.NOTIFICATION_SERVICE***即可。示例代码如下所示：

```java
NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
```

接下来通过***Builder***构造器来创建***Notification***对象，由于Android系统不同版本对通知功能的更新和修改，我们需要使用一个稳定的版本来创建对象，因此我们选用support-v4库中的 ***NotiﬁcationCompat***类的构造器来创建***Notification***对象，代码如下所示：

```java
NotificationCompat.Builder build = new NotificationCompat.Builder(context).build();
```

由于在Android 8.0系统之后，Google推出了通知渠道的概念。 通知渠道是开发人员在创建通知的时候为每个通知指定的渠道，你也 可以理解成是通知类型。属于同一通知渠道的通知可以进行统一管理。 每一个开发人员在开发App的时候都可以自由的创建通知渠道，但是这些通知渠道的控制权 都是掌握在用户手里的，简单点说就是用户可以更改渠道的重要性，从而决定通知的提示状 态，是弹出提示，震动提示，声音提示等等。 这种操作也很好的提高了用户的体验，不会像之前不管是什么通知都会从顶部弹出提示。 所以接下来我们需要创建***NotiﬁcationChannel***，创建的方法也不难，首先创建 ***NotiﬁcationChannel***对象，指定Channel的id、name和通知的重要程度，代码如下所示：

```java
NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",               NotificationManager.IMPORTANCE_DEFAULT);
```

然后使用***NotiﬁcationMannager***的***createNotiﬁcationChannel***方法来添加Channel，代码如下所 示：
由于通知渠道的概念是Android 8.0以上版本新引入的特性，因此在创建通知渠道之前还需要对系 统版本进行简单判断，代码如下所示：

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",               NotificationManager.IMPORTANCE_DEFAULT);
manage.createNotificationChannel(channel);
Notification notification  = new NotificationCompat.Builder(context).build();
manager.notify(1,notification);
}else{
    NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
Notification notification  = new NotificationCompat.Builder(context).build();
    manager.notify(2,notification);
}
```



通过上述操作我们知识创建了一个空的***NotificationCompat.Builder***对象，并没有什么实际作用，我们可以在最终的***build()***方法之前调用多种方法设置***Notiﬁcation***对象，基本设置如下所示：

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
    				.setAutoCancel(true)                
    				.setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.message))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(getString(R.string.ma_notify_title))
                    .setContentText(getString(R.string.ma_notify_text));
```

上述代码中调用了5个设置的方法分别用来设置通知点击后自动消失，通知的时间显示，通知的大图标、小图标、通知内容标题以及显示内容。以上工作全部完成后，就可以通过调用***NotiﬁcationManager***的***notify()***方法让通知显示出 来。***notify()***方法中接收两个参数，第一个参数表示通知所指定的id，第二个参数表示***Notiﬁcation***对象。

很多时候需要我们点击通知后跳转到另外一个活动中去。此时活动的跳转不仅仅需要***Intent***进行，还需要使用***PendingIntent***。代码如下：

```java
Intent intent = new Intent(MainActivity.this, ToActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);
```

创建好***PendingIntent***之后，我们还需要使用***setContentIntent(pendingIntent)***将其设置到通知中。



到这里，通知创建的步骤分析结束，接下来我们通过一个具体例子看一下通知的使用。

文章中使用了***BingView***，若不了解请查看： [BindView学习](http://blog.crazymen.cn/2020/09/14/viewBinding%E5%AD%A6%E4%B9%A0/)



效果图：

![](http://zl.crazymen.cn/images/20201007.gif)



代码展示：

```java
private NotificationManagerCompat manager;
private boolean enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //使用NotificationManagerCompat.from产生的是使用NotificationManagerCompat对象。
        //也可以使用getSystemService(NOTIFICATION_SERVICE)产生NotificationManager对象。
        //用于管理Notification。
        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager = NotificationManagerCompat.from(MainActivity.this);
        
        //检查用户是否允许应用显示通知
        enabled = manager.areNotificationsEnabled();
    }

```



```java
//发出通知
    private void sendNotify() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "This Notification's name", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            Intent intent = new Intent(MainActivity.this, ToActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(MainActivity.this, "notify")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(getString(R.string.ma_notify_title))
                    .setContentText(getString(R.string.ma_notify_text))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.message))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(1, notification);
        } else {
            Intent intent = new Intent(MainActivity.this, ToActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.message))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(getString(R.string.ma_notify_title))
                    .setContentText(getString(R.string.ma_notify_text))
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(2, notification);
        }
    }
```



```java
//适用于各个版本安卓的通知允许设置
    private void requestNotification() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        } else if (Build.VERSION.SDK_INT >= 15) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        startActivity(intent);
    }
```


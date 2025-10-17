package com.stupidbeauty.hxlauncher;

import com.stupidbeauty.codeposition.CodePosition;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Files;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import android.net.Uri;
import android.os.Debug;
import com.stupidbeauty.hxlauncher.datastore.LauncherIconType;
import com.stupidbeauty.victoriafresh.VFile;
import android.service.chooser.ChooserTarget;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.otto.Bus;
import com.stupidbeauty.hxlauncher.application.HxLauncherApplication;
import com.stupidbeauty.hxlauncher.bean.ApplicationNameInternationalizationData;
import com.stupidbeauty.pdflearn.activity.PdfLearnActivity;
import com.stupidbeauty.qtdocchinese.ArticleInfo;
import java.util.ArrayList;
import java.util.HashMap;
import com.stupidbeauty.builtinftp.demo.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.net.Uri;

public class ApplicationAliasApplicationInformationAdapter extends RecyclerView.Adapter<ApplicationAliasApplicationInformationAdapter.ClipViewHolder>
{
  private static final String TAG="ApplicationAliasApplicationInformationAdapter"; //!<输出调试信息时使用的标记。
  private ArrayList<ArticleInfo> articleInfoArrayList; //!<文章信息列表。

  private Bus bus; //!<总线 。

    private final PdfLearnActivity context; //!<上下文。

    public static class ClipViewHolder extends RecyclerView.ViewHolder
    {
      public String activityName; //!<具体的活动名字。
      public String functionName; //!< function name.
      public String packageName; //!<应用包名。
      public Bus bus; //!<消息总线。
      public int clipId=0; //!<本视频对应的剪辑编号。
      public Intent launchIntent; //!<用于启动应用程序的意图。
      public String filePath; //!< The file path.

      /**
      * 将文字加入队列中。
      * @param buttongetText 文字。
      */
      private void enqueueText(String buttongetText)
      {
        interactiveText=interactiveText+buttongetText; //加入到文字后面。
      } //private void enqueueText(String buttongetText)

      private String interactiveText=""; //!<手工校正的交互过程中的文字字符串。

      private void startCallTimer() 
      {
        applyInteractiveText(); //应用交互过程中产生的文字。
      }

      /**
      * 应用交互过程中产生的文字。
      */
      private void applyInteractiveText()
      {
        mTextView.setText(interactiveText); //修改文字。

        interactiveText=""; //交互过程中累积的文字清空。
      } //private void applyInteractiveText()

      private final PdfLearnActivity context; //!<上下文。

      @BindView(R.id.rightTextoperationMethodactTitletextView2) TextView mTextView; //!<文字视图。

      @BindView(R.id.fileprogressBar) ProgressBar fileprogressBar; //!<File progress bar

      @BindView(R.id.receivedLengthTextoperationMethodactTitletextView2) TextView receivedLengthTextoperationMethodactTitletextView2; //!<REceived length text view

      @BindView(R.id.slashTextoperationMethodactTitletextView2) TextView slashTextoperationMethodactTitletextView2; //!<Slash text view

      @BindView(R.id.wholeLengthTextoperationMethodactTitletextView2) TextView wholeLengthTextoperationMethodactTitletextView2; //!<Whole legnth text view

      @BindView(R.id.applicationIconrightimageView2) ImageView applicationIconrightimageView2; //!<图标。

      public ClipViewHolder(View v, final PdfLearnActivity context1)
      {
        super(v);

        ButterKnife.bind(this,v); //视图注入。

        context=context1; //记录上下文。
      } //public ClipViewHolder(TextView v)
      
      /**
      * Request view file.
      */
      private void requestViewFile() 
      {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File playFile = new File(filePath);
        
        Path pathObject=playFile.toPath();

        try
        {
          String fileType=Files.probeContentType(pathObject);
        
          intent.setDataAndType(Uri.fromFile(playFile), fileType);

          context.startActivity(intent);
        }
        catch(IOException e)
        {
        }
      } // private void requestViewFile()
      
      @OnClick({R.id.homeNewsLayout,R.id.launchRipple})
      /**
      * 启动应用。
      */
      public void launchApplication()
      {
        context.hideKeyboard(); // Hide keyboard.
      } //public void launchApplication()
    }

    public ApplicationAliasApplicationInformationAdapter(PdfLearnActivity context1)
    {
      context=context1; //记录上下文。
    }

    public void setBus(Bus bus) 
    {
      this.bus = bus;
    }

    /**
     * 设置文件信息列表。
     * @param articleInfoArrayList 要设置的文件信息列表。
     */
    public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)
    {
        this.articleInfoArrayList = articleInfoArrayList; //记录列表。
    } //public void setArticleInfoArrayList(ArrayList<ArticleInfo> articleInfoArrayList)


    /**
     * 获取条目个数。
     * @return 条目个数。
     */
    public int getItemCount()
    {
      int result=0; //个数。

      if (articleInfoArrayList!=null) //指针有效。
      {
        result=articleInfoArrayList.size();
      } //if (articleInfoArrayList!=null) //指针有效。

      return result;
    } //public int getItemCount()

    /**
     * 绑定视图占位器。
     * @param holder 占位器。
     * @param position 位置。
     */
    public void onBindViewHolder(ApplicationAliasApplicationInformationAdapter.ClipViewHolder holder, int position)
    {
      Log.d(TAG, CodePosition.newInstance().toString()); //Debug.
      ArticleInfo articleInfo=articleInfoArrayList.get(position); //获取对应位置的文章。

      String articleTitle=articleInfo.getApplicationLabel().toString(); //获取文章标题。

      Intent applicationIntent=articleInfo.getLaunchIntent(); //获取启动意图。

      holder.mTextView.setText(articleTitle); //设置新的文字内容。

      holder.launchIntent=applicationIntent; //设置启动意图。
      Log.d(TAG, CodePosition.newInstance().toString() + ", function name: " + articleInfo.getFunctionName()); //Debug.
      holder.packageName=articleInfo.getPackageName(); //获取包名。
      holder.activityName=articleInfo.getActivityName(); //获取活动名字。
      
      holder.functionName=articleInfo.getFunctionName(); // Set function name.

      if (articleInfo.getFunctionName().equals("FileMessage")) //Is file message
      {
        holder.receivedLengthTextoperationMethodactTitletextView2.setText(""+ articleInfo.getReceivedLength() ); //Show received length

        Log.d(TAG, CodePosition.newInstance().toString()+", received length: " + articleInfo.getReceivedLength()); //Debug.
        holder.receivedLengthTextoperationMethodactTitletextView2.setVisibility(View.VISIBLE);
        holder.slashTextoperationMethodactTitletextView2.setVisibility(View.VISIBLE);
        holder.wholeLengthTextoperationMethodactTitletextView2.setVisibility(View.VISIBLE);
        holder.fileprogressBar.setVisibility(View.VISIBLE);

        holder.fileprogressBar.setMax((int)(articleInfo.getFileLength())); //Set max
        holder.fileprogressBar.setProgress(articleInfo.getReceivedLength()); //SEt value
        holder.filePath=articleInfo.getFilePath(); // Set file path.

        Log.d(TAG, CodePosition.newInstance().toString() + ", file length: " + articleInfo.getFileLength()); //Debug.
        holder.wholeLengthTextoperationMethodactTitletextView2.setText(""+articleInfo.getFileLength()); //Show file whole length
      } //if (articleInfo.getFunctionName().equals("FileMessage")) //Is file message
      else //Text message
      {
        holder.receivedLengthTextoperationMethodactTitletextView2.setVisibility(View.INVISIBLE);
        holder.slashTextoperationMethodactTitletextView2.setVisibility(View.INVISIBLE);
        holder.wholeLengthTextoperationMethodactTitletextView2.setVisibility(View.INVISIBLE);
        holder.fileprogressBar.setVisibility(View.INVISIBLE);
      } //else //Text message
    } //public void onBindViewHolder(ClipViewHolder holder,int position)

    /**
     * 创建视图占位器。
     * @param parent 亲代视图。
     * @param viewType 视图类型。
     * @return 创建的占位器。
     */
    public ApplicationAliasApplicationInformationAdapter.ClipViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
      View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_alias_application_info_row, parent,false); //从界面文件中创建出视图。

      ApplicationAliasApplicationInformationAdapter.ClipViewHolder clipViewHolder=new ApplicationAliasApplicationInformationAdapter.ClipViewHolder(v,context);
      clipViewHolder.bus=bus;

      return clipViewHolder;
    } //public ClipViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
}

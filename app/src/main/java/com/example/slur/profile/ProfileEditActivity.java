package com.example.slur.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.post.EditProfileRequest;
import com.example.slur.post.ProfileRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.slur.R.id.tv_thumbnail_title;

//import static com.example.slur.R.id.tv_thumbnail_title;


public class ProfileEditActivity extends AppCompatActivity {
    // EDITED
    ImageView profileImg, addImg;
    TextView emailTv;
    EditText nicknameEt, universityEt, contactEt, introductionEt, majorEt;
    Spinner majorSpinner;

    TextView textView;
    String[] items = {"선택", "전공자", "비전공자"};
    RecyclerView ClipView;
    ClipAdapter clAdapter;
    Button ConfirmBtn;
    RelativeLayout ProfileContainer;

    Bitmap profileImgBitmap;

    private PreferenceHelper preferenceHelper; // EDITED

    private static final int RESULT_LOAD_IMG = 100; // EDITED

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        preferenceHelper = new PreferenceHelper(getApplicationContext()); // EDITED

        initViews();

        // EDITED
        ProfileRequest ownerProfileRequest = new ProfileRequest(preferenceHelper.getUserId(), ownerProfileResponseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(ownerProfileRequest);
    }

    // EDITED
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                profileImgBitmap = getCorrectlyOrientedImage(this, imageUri);
                profileImg.setImageBitmap(profileImgBitmap);
                addImg.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
        }
    }

    // EDITED
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return 90;  // 90도 회전
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    // EDITED
    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int orientation = getOrientation(context, photoUri);

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        srcBitmap = BitmapFactory.decodeStream(is);
        is.close();

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    //테스트 데이터 입니당.
    private ArrayList<VideoClip> makeDefaultClip() {
        ArrayList<VideoClip> list = new ArrayList<>();

        list.add(new VideoClip(null, null, "첫쨰영상"));
        list.add(new VideoClip(null, null, "둘쨰영상"));
        list.add(new VideoClip(null, null, ""));
        return list;
    }

    private void initViews() {
        // EDITED
        profileImg = (ImageView) findViewById(R.id.profile_img);
        addImg = (ImageView) findViewById(R.id.add_img);
        nicknameEt = (EditText) findViewById(R.id.nick_name);
        emailTv = (TextView) findViewById(R.id.email);
        universityEt = (EditText) findViewById(R.id.tv_university);
        majorEt = (EditText) findViewById(R.id.tv_major_2);
        contactEt = (EditText) findViewById(R.id.tv_contact);
        introductionEt = (EditText) findViewById(R.id.tv_introduction);

        //clip view
        ClipView = findViewById(R.id.recycler_view);
        ClipView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //clip 데이터(테스트용)
        ArrayList<VideoClip> list = makeDefaultClip();

        //clip Adapter
        clAdapter = new ClipAdapter(list);
        ClipView.setAdapter(clAdapter);


        ConfirmBtn = findViewById(R.id.btn_modify_confirm);
        ProfileContainer = findViewById(R.id.profile_img_container);
        textView = findViewById(R.id.tv_major_2);
        majorSpinner = findViewById(R.id.spinner_major);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(adapter);

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 1) {
                    textView.setVisibility(View.VISIBLE);
                } else if (position == 2 || position == 0) {
                    textView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("");
            }
        });


        //프로필 사진클릭시 팝업으로 띄우기
        ProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProfileEditPopup(ProfileEditActivity.this).show();

            }
        });


        //수정 버튼 클릭시 진짜 수정 여부 팝업 생성
        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EDITED : get edited information
                final String name = nicknameEt.getText().toString();
                final String university = universityEt.getText().toString();
                final int isMajorIndex = majorSpinner.getSelectedItemPosition();
                final String major = majorEt.getText().toString();
                final String contact = contactEt.getText().toString();
                final String introduction = introductionEt.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(ProfileEditActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isMajorIndex == 0) {
                    Toast.makeText(ProfileEditActivity.this, "전공자 여부를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this, R.style.Theme_AppCompat_Light_Dialog);
                builder.setTitle("프로필을 수정하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // EDITED
                        EditProfileRequest editProfileRequest = new EditProfileRequest(preferenceHelper.getUserId(), name, university, isMajorIndex, major, contact, introduction, profileImgBitmap, editProfileResponseListener);
                        RequestQueue queue = Volley.newRequestQueue(ProfileEditActivity.this);
                        queue.add(editProfileRequest);
                    }
                });
                //취소시 현재 팝업 닫기
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                });
                builder.create().show();
            }
        });

    }

    // clip 어댑터
    private class ClipAdapter extends RecyclerView.Adapter<ClipHolder> {

        ArrayList<VideoClip> list; //테스트용
        ClipAddPopup clipDialog;

        public ClipAdapter(ArrayList<VideoClip> list) {
            this.list = list;
        }

        @Override
        public ClipHolder onCreateViewHolder(ViewGroup parent, int pos) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.activity_video_clip_edit_list_item, null, false);
            return new ClipHolder(view);
        }//뷰 홀더 만들고 반환


        @Override
        public void onBindViewHolder(ClipHolder rowViewHolder, final int pos) {
            VideoClip clip_item = list.get(pos);

            if (!TextUtils.isEmpty(clip_item.getTitle())) { //타이틀값이 null이 아니면 //if (pos != 2)
                rowViewHolder.thumbnailContainer.setVisibility(View.VISIBLE); //리스트 아이템 visible
                rowViewHolder.emptyContainer.setVisibility(View.GONE); //추가하기 UI 는 Gone

                rowViewHolder.thumbnail.setBackgroundResource(R.drawable.profile);//썸네일 임시데이터
                rowViewHolder.title.setHint(clip_item.getTitle()); //타이틀은 힌트로 설정

                rowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this,
                                R.style.Theme_AppCompat_Light_Dialog);
                        builder.setTitle("영상을 삭제하시겠습니까?");
                        builder.setMessage("삭제된 영상은 다시 되돌릴수 없습니다.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //삭제기능 구현
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //취소시 팝업닫기
                            }
                        });
                        builder.create().show();
                    }
                });
            } else { //타이틀값이 null일때
                rowViewHolder.thumbnailContainer.setVisibility(View.GONE); //추가하기 이미지만 보여주기 위해 기존 레이아웃은 Gone
                rowViewHolder.thumbnail.setBackgroundResource(R.drawable.profile);//썸네일 임시데이터
                rowViewHolder.emptyContainer.setVisibility(View.VISIBLE);
                rowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clipDialog = new ClipAddPopup(ProfileEditActivity.this);//영상 추가를 위한 팝업
                        clipDialog.setOnCancelListener(CancelListener);
                        clipDialog.setOnUploadListener(UploadListener);
                        clipDialog.show();
                    }
                });
            }
        }

        //취소 버튼 클릭시
        View.OnClickListener CancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipDialog.dismiss();
            }
        };

        //업로드 버튼 클릭시
        View.OnClickListener UploadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipDialog.dismiss();
            }
        };

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    public class ClipHolder extends RecyclerView.ViewHolder {
        RelativeLayout thumbnailContainer;
        RelativeLayout emptyContainer;
        ImageView emptyImg;
        ImageView thumbnail;
        TextView title;

        public ClipHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.img_thumbnail);//추가 썸네일 이미지
            title = itemView.findViewById(tv_thumbnail_title); //추가 썸네일 타이틀(힌트)
            thumbnailContainer = itemView.findViewById(R.id.thumb_container); //삭제할 클립 리스트 아이템
            emptyContainer = itemView.findViewById(R.id.thumb_add_container);//클립 추가 리스트 아이템
            emptyImg = itemView.findViewById(R.id.img_add_thumbnail);//영상 추가하기 이미지
        }
    }

    // EDITED
    Response.Listener<String> ownerProfileResponseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                if (success) {
                    JSONObject profile = jsonObject.getJSONObject("profile");

                    String base64String = profile.getString("profile_img");
                    if (!base64String.equals("")) {
                        addImg.setVisibility(View.GONE);
                        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                        profileImgBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profileImg.setImageBitmap(profileImgBitmap);
                    }
                    nicknameEt.setText(profile.getString("name"));
                    emailTv.setText(profile.getString("email"));
                    universityEt.setText(profile.getString("aff"));
                    if (profile.getString("if_major").equals("")) {
                    } else if (profile.getString("if_major").equals("0")) {
                        majorSpinner.setSelection(2);
                    } else {
                        majorSpinner.setSelection(1);
                    }
                    majorEt.setText(profile.getString("major"));
                    contactEt.setText(profile.getString("phone"));
                    introductionEt.setText(profile.getString("info"));

                } else {//로그인 실패시
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (JSONException e) {
                Log.d("Test", response);
                e.printStackTrace();
            }
        }
    };

    // EDITED
    Response.Listener<String> editProfileResponseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                if (success) {
                    Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_LONG).show();
                    finish();

                } else {//로그인 실패시
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (JSONException e) {
                Log.d("Test", response);
                e.printStackTrace();
            }
        }
    };

    // EDITED
    public void getImageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    // EDITED
    public void setToDefaultImage() {
        profileImg.setImageResource(R.drawable.profile);
        addImg.setVisibility(View.VISIBLE);
        profileImgBitmap = null;
    }
}
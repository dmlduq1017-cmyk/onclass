package com.example.project;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView fullVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 💡 전체화면 설정 (항상 super 전에 호출)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);

        // 🔗 View 바인딩
        fullVideoView = findViewById(R.id.fullVideoView);

        // 🎬 인텐트로부터 영상 리소스 ID 받기
        int videoResId = getIntent().getIntExtra("videoResId", R.raw.vedio1);

        // 🎥 비디오 URI 생성 및 재생 시작
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
        fullVideoView.setVideoURI(videoUri);

        // ▶️ 컨트롤러 연결
        MediaController controller = new MediaController(this);
        controller.setAnchorView(fullVideoView);
        fullVideoView.setMediaController(controller);

        // ⏯️ 자동 재생
        fullVideoView.setOnPreparedListener(mp -> fullVideoView.start());
    }
}

<template>
  <div>
    <h1>引体向上计数器</h1>
    <video ref="video" width="640" height="480"></video>
    <canvas ref="canvas" width="640" height="480"></canvas>
    <button @click="startCounting" :disabled="pullUpStarted">开始训练</button>
    <button @click="stopCounting" :disabled="!pullUpStarted">停止训练</button>
    <button @click="saveTraining">保存训练数据</button>
    <p id="status">{{ status }}</p>
    <p>Pull Ups: {{ pullUpCounter }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      status: "模型加载中...",
      video: null,
      canvas: null,
      poseNet: null,
      pose: null,
      skeleton: null,
      pullUpCounter: 0,
      pullUpDone: false,
      pullUpStarted: false,
      facingMode: "environment",
    };
  },
  mounted() {
    this.video = this.$refs.video;
    this.canvas = this.$refs.canvas;
    this.poseNet = ml5.poseNet(this.video, this.modelLoad);
    this.poseNet.on("pose", this.gotPoses);
  },
  methods: {
    modelLoad() {
      console.log("poseNet ready");
      this.pullUpCounter = 0;
      this.status = "模型加载成功，可以开始训练了！";
    },
    gotPoses(poses) {
      if (poses.length > 0) {
        this.pose = poses[0].pose;
        this.skeleton = poses[0].skeleton;
      }
    },
    startCounting() {
      this.pullUpStarted = true;
      this.pullUpCounter = 0;
    },
    stopCounting() {
      this.pullUpStarted = false;
    },
    saveTraining() {
      const data = {
        pullUpCounter: this.pullUpCounter,
      };
      // 发送数据到后端
      // ...
    },
  },
};
</script>
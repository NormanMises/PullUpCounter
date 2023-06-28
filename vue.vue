<template>
  <div>
    <video ref="video" width="640" height="480"></video>
    <canvas ref="canvas" width="640" height="480"></canvas>
    <button @click="startCounting" :disabled="pullUpStarted">Start Training</button>
    <button @click="stopCounting" :disabled="!pullUpStarted">Stop Training</button>
    <button @click="sendData" :disabled="!pullUpDone">Save this training</button>
    <p>Pull Ups: {{ pullUpCounter }}</p>
    <p id="status">{{ status }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      video: null,
      canvas: null,
      poseNet: null,
      pose: null,
      skeleton: null,
      pullUpCounter: 0,
      pullUpDone: false,
      pullUpStarted: false,
      facingMode: "environment",
      status: "",
    };
  },
  mounted() {
    this.video = this.$refs.video;
    this.canvas = this.$refs.canvas;
    this.setup();
  },
  methods: {
    setup() {
      this.video = this.$refs.video;
      this.canvas = this.$refs.canvas;

      this.video.width = 640;
      this.video.height = 480;

      this.poseNet = ml5.poseNet(this.video, this.modelLoad);
      this.poseNet.on("pose", this.gotPoses);

      this.startButton = document.querySelector("#startButton");
      this.stopButton = document.querySelector("#stopButton");
      this.sendButton = document.querySelector("#sendButton");

      this.startButton.addEventListener("click", this.startCounting);
      this.stopButton.addEventListener("click", this.stopCounting);
      this.sendButton.addEventListener("click", this.sendData);
    },
    modelLoad() {
      console.log("poseNet ready");
      this.pullUpCounter = 0;
      this.status = "Model loaded successfully. You can start training now!";
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
      this.startButton.setAttribute("disabled", "");
      this.stopButton.removeAttribute("disabled");
    },
    stopCounting() {
      this.pullUpStarted = false;
      this.stopButton.setAttribute("disabled", "");
      this.startButton.removeAttribute("disabled");
    },
    sendData() {
      const data = {
        pullUpCounter: this.pullUpCounter,
      };
      this.$http.post("/backend-endpoint", data)
        .then(response => {
          console.log(response);
          this.showSuccessMessage();
        })
        .catch(error => {
          console.error(error);
          this.showErrorMessage();
        });
    },
    showSuccessMessage() {
      alert("Data sent successfully!");
    },
    showErrorMessage() {
      alert("Failed to send data. Please try again.");
    },
    draw() {
      if (this.pose) {
        const ctx = this.canvas.getContext("2d");
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // Draw video on canvas
        ctx.drawImage(this.video, 0, 0, this.canvas.width, this.canvas.height);

        // Draw keypoints
        for (let j = 0; j < this.pose.keypoints.length; j++) {
          const keypoint = this.pose.keypoints[j];
          if (keypoint.score > 0.5) {
            ctx.fillStyle = "red";
            ctx.beginPath();
            ctx.arc(keypoint.position.x, keypoint.position.y, 16, 0, 2 * Math.PI);
            ctx.fill();
          }
        }

        // Draw skeleton
        for (let j = 0; j < this.skeleton.length; j++) {
          const partA = this.skeleton[j][0];
          const partB = this.skeleton[j][1];
          ctx.strokeStyle = "blue";
          ctx.lineWidth = 5;
          ctx.beginPath();
          ctx.moveTo(partA.position.x, partA.position.y);
          ctx.lineTo(partB.position.x, partB.position.y);
          ctx.stroke();
        }
      }

      if (this.pullUpStarted) {
        this.updatePullUpCounter();
      }
    },
    calculateKeypointDistance(keypoint1, keypoint2) {
      return Math.dist(
        keypoint1.position.x,
        keypoint1.position.y,
        keypoint2.position.x,
        keypoint2.position.y
      );
    },
    calculateDistance(keypoint1, keypoint2) {
      return Math.dist(
        keypoint1.x,
        keypoint1.y,
        keypoint2.x,
        keypoint2.y
      );
    },
    calculateAngle(pointA, pointB, pointC) {
      const AB = this.calculateDistance(pointA, pointB);
      const BC = this.calculateDistance(pointB, pointC);
      const AC = this.calculateDistance(pointA, pointC);
      return Math.acos((AB * AB + BC * BC - AC * AC) / (2 * AB * BC)) * 180 / Math.PI;
    },
    isPullUpDone() {
      if (!this.pose) {
        return false;
      }

      const head = this.pose.nose;
      const leftHand = this.pose.leftWrist;
      const rightHand = this.pose.rightWrist;
      const leftElbow = this.pose.leftElbow;
      const rightElbow = this.pose.rightElbow;
      const leftShoulder = this.pose.leftShoulder;
      const rightShoulder = this.pose.rightShoulder;

      if (
        head &&
        leftHand &&
        rightHand &&
        leftElbow &&
        rightElbow &&
        leftShoulder &&
        rightShoulder &&
        head.confidence > 0.5 &&
        leftHand.confidence > 0.5 &&
        rightHand.confidence > 0.5
      ) {
        const slope = (rightHand.y - leftHand.y) / (rightHand.x - leftHand.x);
        const intercept = leftHand.y - slope * leftHand.x;
        const headYOnLine = slope * head.x + intercept;
        const leftAngle = this.calculateAngle(leftShoulder, leftElbow, leftHand);
        const rightAngle = this.calculateAngle(rightShoulder, rightElbow, rightHand);

        if (
          head.y < headYOnLine &&
          leftElbow.y > leftShoulder.y &&
          rightElbow.y > rightShoulder.y
        ) {
          return true;
        }
      }
      return false;
    },
    updatePullUpCounter() {
      if (this.isPullUpDone() && !this.pullUpDone) {
        this.pullUpCounter++;
        this.pullUpDone = true;
      } else if (!this.isPullUpDone()) {
        this.pullUpDone = false;
      }
    },
  },
  created() {
    this.pullUpCounter = 0;
    this.status = "Loading model...";
  },
  beforeDestroy() {
    this.poseNet.removeAllListeners();
  },
};
</script>

<style scoped>
#videoCanvas {
  display: block;
  margin: 0 auto;
}
</style>

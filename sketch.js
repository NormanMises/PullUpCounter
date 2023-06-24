let video;
let poseNet;
let pose;
let skeleton;
let pullUpCounter = 0;
let pullUpDone = false;

function setup() {
    video = createCapture({video: {facingMode: {exact: "environment"}}}, () => {
        // 在视频加载后获取视频的宽度和高度
        const videoWidth = video.width;
        const videoHeight = video.height;

        // 设置画布大小为视频的宽度和高度
        // createCanvas(videoWidth, videoHeight);
        createCanvas(windowWidth, windowHeight);
        video.hide();

        poseNet = ml5.poseNet(video, modelLoad);
        poseNet.on('pose', gotPoses);
    });
}


function gotPoses(poses) {
    console.log(poses);

    if (poses.length > 0) {
        pose = poses[0].pose;
        skeleton = poses[0].skeleton;
    }
}

function modelLoad() {
    console.log('poseNet ready')
}

function draw() {
    // 将画布的坐标系移动到画布的右上角
    // translate(video.width, 0);
    // 沿着x轴翻转画布
    // scale(-1, 1);
    // 在画布上绘制视频
    // image(video, 0, 0, video.width, video.height);

    // 计算视频的缩放比例
    const scaleFactor = min(width / video.width, height / video.height);

    // 计算视频在画布中的位置和尺寸
    const scaledWidth = video.width * scaleFactor;
    const scaledHeight = video.height * scaleFactor;

    const x = (width - scaledWidth) / 2;
    const y = (height - scaledHeight) / 2;

    // 在画布上绘制视频
    image(video, x, y, scaledWidth, scaledHeight);

    // 如果检测到人体姿势
    if (pose) {
        // 计算左右肩之间的距离
        let distance = calculateDistance(pose.keypoints[5], pose.keypoints[6]);
        distance = min(distance, 32);
        // console.log(distance);

        // 绘制关键点
        drawKeypoints(distance);
        // 绘制骨架
        drawSkeleton();
        drawLineBetweenHands();
        // 更新引体向上计数器
        updatePullUpCounter();
    }
}

function drawKeypoints(distance) {
    for (let j = 0; j < pose.keypoints.length; j++) {
        let keypoint = pose.keypoints[j];
        // Only draw an ellipse is the pose probability is bigger than 0.2
        if (keypoint.score > 0.2) {
            fill(255, 0, 0);
            noStroke();
            ellipse(keypoint.position.x, keypoint.position.y, distance);
        }
    }
}

function drawSkeleton() {
    for (let j = 0; j < skeleton.length; j++) {
        let partA = skeleton[j][0];
        let partB = skeleton[j][1];
        stroke(0, 255, 0);
        strokeWeight(2);
        line(partA.position.x, partA.position.y, partB.position.x, partB.position.y);
    }
}


function drawLineBetweenHands() {
    let leftHand = pose.leftWrist;
    let rightHand = pose.rightWrist;
    if (leftHand.confidence > 0.5 && rightHand.confidence > 0.5) {
        stroke(255, 0, 0);
        strokeWeight(2);
        line(leftHand.x, leftHand.y, rightHand.x, rightHand.y);
    }
}


// 计算两个关键点之间的距离
function calculateDistance(keypoint1, keypoint2) {
    return dist(keypoint1.position.x, keypoint1.position.y, keypoint2.position.x, keypoint2.position.y);
}

// 判断是否完成引体向上
function isPullUpDone() {
    // 获取头部、左手和右手的位置
    const head = pose.nose;
    const leftHand = pose.leftWrist;
    const rightHand = pose.rightWrist;

    // 判断头部、左手和右手的位置是否存在
    if (head.confidence > 0.5 && leftHand.confidence > 0.5 && rightHand.confidence > 0.5) {
        // 计算两手连线的斜率和截距
        const slope = (rightHand.y - leftHand.y) / (rightHand.x - leftHand.x);
        const intercept = leftHand.y - slope * leftHand.x;

        // 计算头部在两手连线上的y坐标
        const headYOnLine = slope * head.x + intercept;
        // console.log(head.y, headYOnLine)

        // 判断头部是否在两手连线上方且距离两手连线的距离大于0
        if (head.y < headYOnLine) {
            // console.log('+++++++++++', head.y, headYOnLine)
            return true; // 引体向上完成
        }
    }

    return false; // 未满足引体向上完成的条件
}


function updatePullUpCounter() {
    if (isPullUpDone() && !pullUpDone) {
        pullUpCounter++;
        pullUpDone = true;
    } else if (!isPullUpDone()) {
        pullUpDone = false;
    }
    textSize(32);
    fill(255, 0, 0);
    // 将画布的坐标系恢复到正常状态
    // scale(-1, 1);
    // text("Pull Ups: " + pullUpCounter, -video.width + 10, 30);
    text("Pull Ups: " + pullUpCounter, 10, 30);
}
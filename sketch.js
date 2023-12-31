// 创建距离计算工厂
class DistanceCalculatorFactory {
    // 创建计算关键点距离的方法
    static createKeypointDistanceCalculator() {
        return new KeypointDistanceCalculator();
    }

    // 创建计算一般距离的方法
    static createDistanceCalculator() {
        return new DistanceCalculator();
    }
}

// 关键点距离计算器类
class KeypointDistanceCalculator {
    calculate(keypoint1, keypoint2) {
        return dist(
            keypoint1.position.x,
            keypoint1.position.y,
            keypoint2.position.x,
            keypoint2.position.y
        );
    }
}

// 一般距离计算器类
class DistanceCalculator {
    calculate(point1, point2) {
        return dist(point1.x, point1.y, point2.x, point2.y);
    }
}


class MessageNotifier {
    constructor() {
        this.observers = [];
    }

    addObserver(observer) {
        this.observers.push(observer);
        // console.log(this.observers);
    }

    removeObserver(observer) {
        this.observers = this.observers.filter((obs) => obs !== observer);
    }

    notifySuccess() {
        this.observers.forEach((observer) => observer.showSuccessMessage());
    }

    notifyError() {
        this.observers.forEach((observer) => observer.showErrorMessage());
    }

    notifyZero() {
        this.observers.forEach((observer) => observer.showZeroMessage());
    }
}

// 观察者对象
const msgObserver = {
    showSuccessMessage() {
        alert("数据保存成功！");// 空方法或其他处理逻辑
    },
    showErrorMessage() {
        alert("数据保存失败，请重试。");
    },
    showZeroMessage() {
        alert("无法保存空数据！");
    },
};

let video;
let poseNet;
let pose, skeleton;
let pullUpCounter = 0;
let pullUpDone = false;
let facingMode = "environment"; // 默认使用后置摄像头
// let switchButton;
let startButton;
let stopButton;
let sendButton;
let pullUpStarted = false;

let keypointDistanceCalculator = DistanceCalculatorFactory.createKeypointDistanceCalculator();
let distanceCalculator = DistanceCalculatorFactory.createDistanceCalculator();

// 创建主题对象
let messageNotifier = new MessageNotifier();


function setup() {
    createCanvas(640, 480);
    video = createCapture({
        video: {
            facingMode: facingMode
        }
    });
    video.hide();

    poseNet = ml5.poseNet(video, modelLoad);
    poseNet.on('pose', gotPoses);

    // console.log('window: ', windowWidth, windowHeight);
    // console.log('video: ', videoWidth, videoHeight);


    // 创建开始计数按钮
    startButton = createButton("开始训练");
    startButton.elt.className = "btn btn-primary";
    startButton.position(10, video.height);
    startButton.mousePressed(startCounting);

    // 创建停止计数按钮
    stopButton = createButton("结束训练");
    stopButton.elt.className = "btn btn-secondary";
    stopButton.position(10, video.height + 50);
    stopButton.mousePressed(stopCounting);

    sendButton = createButton("保存训练数据");
    sendButton.elt.className = "btn btn-info";
    sendButton.position(10, video.height + 100);
    sendButton.mousePressed(() => {
        if (pullUpCounter > 0) {
            // 创建一个包含pullUpCounter的数据对象
            const data = {
                pullUpCounter: pullUpCounter
            };
            // 调用sendData函数发送数据到后端

            sendData(data);
        } else {
            showZeroMsg();
        }
    });
    // 添加观察者
    messageNotifier.addObserver(msgObserver);
}

function modelLoad() {
    console.log('poseNet ready');
    pullUpCounter = 0;
    document.querySelector('#status').innerHTML = '模型加载成功，可以开始训练了！';
}


function gotPoses(poses) {
    // console.log(poses);
    if (poses.length > 0) {
        pose = poses[0].pose;
        skeleton = poses[0].skeleton;
    }
}

function startCounting() {
    pullUpStarted = true;
    // 设置计数器为0
    pullUpCounter = 0;
    // 禁用开始计数按钮
    startButton.attribute("disabled", "");
    // 启用停止计数按钮
    stopButton.removeAttribute("disabled");
}

function stopCounting() {
    // 设置计时器开始标志为false
    pullUpStarted = false;
    // 禁用停止计数按钮
    stopButton.attribute("disabled", "");
    // 启用开始计数按钮
    startButton.removeAttribute("disabled");
}

// 将数据作为JSON发送到后端
function sendData(data) {
    // 创建一个XMLHttpRequest对象
    const xhr = new XMLHttpRequest();

    // 设置请求方法和URL
    xhr.open("POST", "/backend-endpoint");

    // 设置请求头（根据需要进行设置）
    xhr.setRequestHeader("Content-Type", "application/json");

    // 监听响应状态变化
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // 请求成功
                console.log(xhr.responseText);
                showSuccessMsg();
            } else {
                // 请求失败
                console.error("Error:", xhr.status);
                showErrorMsg();
            }
        }
    };
    // 将数据转换为JSON字符串并发送
    xhr.send(JSON.stringify(data));
}

// 显示发送成功消息
function showSuccessMsg() {
    messageNotifier.notifySuccess();
}

// 显示发送失败消息
function showErrorMsg() {
    messageNotifier.notifyError();
}

function showZeroMsg() {
    messageNotifier.notifyZero();
}

function draw() {
    // 将画布的坐标系移动到画布的右上角
    // translate(video.width, 0);
    // 沿着x轴翻转画布
    // scale(-1, 1);
    // 在画布上绘制视频
    // image(video, 0, 0, video.width, video.height);


    // 在画布的上居中位置绘制视频
    // image(video, (width - video.width) / 2, 0);
    image(video, 0, 0);
    if (pose) {
        // 计算左右肩之间的距离
        // let distance = calculateKeypointDistance(pose.keypoints[5], pose.keypoints[6]);
        let distance = keypointDistanceCalculator.calculate(pose.keypoints[5], pose.keypoints[6]);
        distance = min(distance, 16);
        // console.log(distance);

        // 绘制关键点
        drawKeypoints(distance);
        // 绘制骨架
        drawSkeleton();
        drawLineBetweenHands();
    }

    if (pullUpStarted) {
        updatePullUpCounter();
    }

    textSize(30);
    fill(255, 0, 0);
    // 将画布的坐标系恢复到正常状态
    // scale(-1, 1);
    // text("Pull Ups: " + pullUpCounter, -video.width + 10, 30);
    text("Pull Ups: " + pullUpCounter, 0, 30);

    // // 计算视频的缩放比例
    // const scaleFactor = min(width / video.width, height / video.height);

    // // 计算视频在画布中的位置和尺寸
    // const scaledWidth = video.width * scaleFactor;
    // const scaledHeight = video.height * scaleFactor;

    // const x = (width - scaledWidth) / 2;
    // const y = (height - scaledHeight) / 2;

    // // 在画布上绘制视频
    // image(video, x, y, scaledWidth, scaledHeight);


}

function drawKeypoints(distance) {
    for (let j = 0; j < pose.keypoints.length; j++) {
        let keypoint = pose.keypoints[j];
        // Only draw an ellipse is the pose probability is bigger than 0.2
        if (keypoint.score > 0.5) {
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
        stroke(0, 0, 255);
        strokeWeight(5);
        line(partA.position.x, partA.position.y, partB.position.x, partB.position.y);
    }
}


function drawLineBetweenHands() {
    let leftHand = pose.leftWrist;
    let rightHand = pose.rightWrist;
    if (leftHand.confidence > 0.5 && rightHand.confidence > 0.5) {
        stroke(255, 0, 0);
        strokeWeight(5);
        line(leftHand.x, leftHand.y, rightHand.x, rightHand.y);
    }
}


// 计算两个关键点之间的距离
function calculateKeypointDistance(keypoint1, keypoint2) {
    return dist(keypoint1.position.x, keypoint1.position.y, keypoint2.position.x, keypoint2.position.y);
}

function calculateDistance(keypoint1, keypoint2) {
    return dist(keypoint1.x, keypoint1.y, keypoint2.x, keypoint2.y);
}

// 计算三个关键点所形成的角度
function calculateAngle(pointA, pointB, pointC) {
    const AB = distanceCalculator.calculate(pointA, pointB);
    const BC = distanceCalculator.calculate(pointB, pointC);
    const AC = distanceCalculator.calculate(pointA, pointC);
    return Math.acos((AB * AB + BC * BC - AC * AC) / (2 * AB * BC)) * 180 / Math.PI;
}

// 判断是否完成引体向上
function isPullUpDone() {
    // 判断 pose 对象是否存在
    if (!pose) {
        return false;
    }

    // 获取头部、左手和右手的位置
    const head = pose.nose;
    const leftHand = pose.leftWrist;
    const rightHand = pose.rightWrist;
    const leftElbow = pose.leftElbow;
    const rightElbow = pose.rightElbow;
    const leftShoulder = pose.leftShoulder;
    const rightShoulder = pose.rightShoulder;

    // 判断头部、左手和右手的位置是否存在
    if (head && leftHand && rightHand && leftElbow && rightElbow && leftShoulder && rightShoulder && head.confidence > 0.5 && leftHand.confidence > 0.5 && rightHand.confidence > 0.5) {
        // 计算两手连线的斜率和截距
        const slope = (rightHand.y - leftHand.y) / (rightHand.x - leftHand.x);
        const intercept = leftHand.y - slope * leftHand.x;

        // 计算头部在两手连线上的y坐标
        const headYOnLine = slope * head.x + intercept;

        // 计算肩、肘、腕三点所形成的角
        // const leftAngle = calculateAngle(leftShoulder, leftElbow, leftHand);
        // const rightAngle = calculateAngle(rightShoulder, rightElbow, rightHand);

        // 判断头部是否在两手连线上方且距离两手连线的距离大于0
        if (head.y < headYOnLine && leftElbow.y > leftShoulder.y && rightElbow.y > rightShoulder.y) {
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
}


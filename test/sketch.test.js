//npx ava
const test = require('ava');

function calculateDistance(keypoint1, keypoint2) {
    return dist(keypoint1.x, keypoint1.y, keypoint2.x, keypoint2.y);
}

// 辅助函数，用于计算距离
function dist(x1, y1, x2, y2) {
    const dx = x2 - x1;
    const dy = y2 - y1;
    return Math.sqrt(dx * dx + dy * dy);
}

// 编写测试用例
test('calculateDistance returns the correct distance', (t) => {
    // 定义测试输入
    const keypoint1 = {x: 0, y: 0};
    const keypoint2 = {x: 3, y: 4};

    // 执行被测试函数
    const distance = calculateDistance(keypoint1, keypoint2);

    // 断言函数的返回值与期望值相等
    t.is(distance, 5);
});


function calculateAngle(pointA, pointB, pointC) {
    const AB = calculateDistance(pointA, pointB);
    const BC = calculateDistance(pointB, pointC);
    const AC = calculateDistance(pointA, pointC);
    return Math.acos((AB * AB + BC * BC - AC * AC) / (2 * AB * BC)) * 180 / Math.PI;
}

// 定义测试用例
test('calculateAngle calculates the angle correctly', (t) => {
    // 定义测试数据
    const pointA = {x: 3, y: 0};
    const pointB = {x: 0, y: 0};
    const pointC = {x: 0, y: 4};


    // 调用 calculateAngle 函数计算角度
    const angle = calculateAngle(pointA, pointB, pointC);

    // 断言计算结果是否与预期相符
    t.is(angle, 90);
});
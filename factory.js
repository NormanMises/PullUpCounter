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
export { DistanceCalculatorFactory };
// 使用示例
const keypoint1 = { position: { x: 0, y: 0 } };
const keypoint2 = { position: { x: 3, y: 4 } };
const point1 = { x: 1, y: 2 };
const point2 = { x: 5, y: 6 };

const keypointDistanceCalculator = DistanceCalculatorFactory.createKeypointDistanceCalculator();
const distanceCalculator = DistanceCalculatorFactory.createDistanceCalculator();

const keypointDistance = keypointDistanceCalculator.calculate(keypoint1, keypoint2);
const distance = distanceCalculator.calculate(point1, point2);

console.log("Keypoint Distance:", keypointDistance);
console.log("Distance:", distance);

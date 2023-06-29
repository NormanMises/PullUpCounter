// 导入被测试的函数
// const { calculateKeypointDistance } = require('../sketch');
import { calculateKeypointDistance } from '/sketch.js';
// 编写测试用例
describe('calculateKeypointDistance', () => {
    test('should calculate distance between two keypoints correctly', () => {
        // 模拟输入数据
        let keypoint1 = { position: { x: 1, y: 0 } };
        let keypoint2 = { position: { x: 2, y: 0 } };

        // 调用被测试的函数
        const distance = calculateKeypointDistance(keypoint1, keypoint2);
        console.log(distance);


        // 断言期望的结果
        expect(distance).toBe(1.0); // 假设期望的距离为5
    });
});

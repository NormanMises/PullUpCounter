// 导入被测试的函数
const { calculateKeypointDistance } = require('../sketch');

// 编写测试用例
describe('calculateKeypointDistance', () => {
    test('should calculate distance between two keypoints correctly', () => {
        // 模拟输入数据
        const keypoint1 = { position: { x: 1, y: 2 } };
        const keypoint2 = { position: { x: 4, y: 6 } };

        // 调用被测试的函数
        const distance = calculateKeypointDistance(keypoint1, keypoint2);


        // 断言期望的结果
        expect(distance).toBe(5); // 假设期望的距离为5
    });
});

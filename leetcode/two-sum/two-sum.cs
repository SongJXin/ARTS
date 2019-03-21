
//Runtime: 384 ms, faster than 54.24% of C# online submissions for Two Sum.
//Memory Usage: 28.7 MB, less than 95.53% of C# online submissions for Two Sum.

public class Solution {
    public int[] TwoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0; i < nums.Length;i ++){
            for(int j = i + 1; j < nums.Length; j++){
                if (target - nums[i] == nums[j]) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }
}
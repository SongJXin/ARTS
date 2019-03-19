# Runtime: 1160 ms, faster than 31.89% of Python3 online submissions for Two Sum.
# Memory Usage: 13.8 MB, less than 27.63% of Python3 online submissions for Two Sum.
class Solution1:
    def twoSum(self, nums, target):
        for num in nums:
            if target - num in nums[nums.index(num)-len(nums)+1:]:
                return [nums.index(num) , nums[nums.index(num)-len(nums)+1:].index(target - num)+nums.index(num)+1]

#Runtime: 7816 ms, faster than 5.01% of Python3 online submissions for Two Sum.
#Memory Usage: 13.9 MB, less than 23.82% of Python3 online submissions for Two Sum.
class Solution2:
    def twoSum(self, nums, target):
        i = 0
        while i < len(nums):
            difference = target - nums[i]
            j = i + 1
            while j < len(nums):
                if nums[j] == difference:
                    return[i,j]
                j = j + 1
            i = i + 1
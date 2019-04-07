//Runtime: 48 ms, faster than 65.42% of Go online submissions for Palindrome Number.
//Memory Usage: 6.1 MB, less than 6.19% of Go online submissions for Palindrome Number.
func isPalindrome(x int) bool {
    if x < 0{
		return false
	}
	var inter []int
	for x > 0{
		inter = append(inter, x % 10)
		x = x / 10
	}
	for j := 0; j < len(inter) ; j += 1{
		if inter[j] != inter[len(inter) - j - 1]{
			return false
		}
	}
	return true
}
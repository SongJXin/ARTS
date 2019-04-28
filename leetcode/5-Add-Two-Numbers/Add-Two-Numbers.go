//Runtime: 12 ms, faster than 100.00% of Go online submissions for Add Two Numbers.
//Memory Usage: 5 MB, less than 9.44% of Go online submissions for Add Two Numbers.

/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */
func addTwoNumbers(l1 *ListNode, l2 *ListNode) *ListNode {
	var lsum ListNode
	carry := false
	if l1.Next==nil&&l2.Next!=nil {
		var lnew ListNode
		lnew.Val = 0
		lnew.Next = nil
		l1.Next = &lnew
	}
	if l2.Next==nil&&l1.Next!=nil {
		var lnew ListNode
		lnew.Val = 0
		lnew.Next = nil
		l2.Next = &lnew
	}
	sum := l1.Val + l2.Val
	if sum >= 10 {
		carry = true
		sum = sum % 10
	}
	lsum.Val = int(sum)
	if carry {
		if l1.Next != nil{
			l1.Next.Val += 1
		} else {
			var lnew ListNode
			lnew.Val = 1
			lnew.Next = nil
			lsum.Next = &lnew
			return &lsum
		}
	}
	if l1.Next!=nil {
		lsum.Next = addTwoNumbers(l1.Next, l2.Next)
	}else{
		lsum.Next=nil
	}
	return &lsum
}
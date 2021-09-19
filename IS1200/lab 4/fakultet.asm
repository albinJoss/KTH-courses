
.text 
addi $a0, $0, 8 # n = 6
addi $a3, $0, 1 #i
addi $v1, $0, 0 #v1 initierad
addi $v0, $0, 0 #v0 initierad 
addi $a2, $a0, 0 #a2 initierad
addi $a1, $0, 1

beq $a0, $0, noll
add $0, $0, $0

LOOP:
	beq $a0, $a3, done2
	add $0, $0, $0	#NOP
	
	beq $a0, $0, STOP
	add $0, $0, $0
	
	
	MUL:
	beq $a1, $0, done1
	add $0, $0, $0
	
	add $v1, $v1, $a2
	addi $a1, $a1, -1
	
	beq $0, $0, MUL
	add $0, $0, $0
	
	done1:
	add $a2, $v1, $0
	addi $a0, $a0, -1
	addi $a1, $a0, -1
	addi $v0, $0, 0
	
	beq $0, $0, LOOP
	add $0, $0, $0
		
done2:
	add $v0, $v0, $v1
	beq $0, $0, STOP
	add $0, $0 $0
	
noll:
	addi $v0, $0, 1
	
STOP:
	beq $0, $0, STOP
	add $0, $0, $0

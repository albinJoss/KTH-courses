.text
addi $a0, $0, 8 #n
addi $v0, $v0, 1 #resultat
addi $a1, $0, 1 # i

beq $a0, $0, noll
add $0, $0, $0

LOOP:
	beq $a0, $a1, done
	add $0, $0, $0
	
	mul $v0, $v0, $a0
	addi $a0, $a0, -1
	
	beq $0, $0, LOOP
	add $0, $0, $0
noll:
	addi $v0, $0, 1
	
done: 
	beq $0, $0, done
	add $0, $0, $0
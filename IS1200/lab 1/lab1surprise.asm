  # labwork.S
  # Written 2015-2017 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1.
  # The macros PUSH and POP are in the public domain.
  # Please add your own code at the end of the file.

  #
  # Please keep the two macros PUSH and POP unchanged
  #
  	.global time2string
  	.global delay
#.macro	PUSH reg
	#addi	$sp,$sp,-4
	#sw	\reg,0($sp)
#.endm

#.macro	POP reg
#	lw	\reg,0($sp)
#	addi	$sp,$sp,4
#.endm
  #
  # Please add your own code below this line
  #
  	.data 
  	.align 2
  	.text 
  	
hexasc:

	addi	$t0, $0, 0x09	#l?gger in bitsen 0000 1001 i t0 f?r j?mf?relse senare 
	andi	$a0, $a0, 0x0f 	#maskar alla bits f?rutom de fyra sista
	andi	$v0, $v0, 0x0	#maskar alla bitar i V0
	ble	$a0, $t0, LT 	#om a0<9 g? till LT
	nop
	add 	$v0, $a0, 0x37	#adderar 0x37 till v0 f?r att f? r?tt ascii kod n?r a0>9
	jr	$ra		#returnernar
	nop
LT:
	add	$v0, $a0, 0x30	#adderar 0x30 till v0 f?r att f? den korrekta ascii koden n?r a0<=9
	jr	$ra
	nop
	
delay:

	#PUSH($ra)
	addi	$sp,$sp,-4
	sw	$ra ,0($sp)
	andi 	$t1, $t1, 0x0
	#addi	$t1, $t1, 0	#anv?nds f?r att best?mma hur m?nga g?nger den ska g? igenom f?ljande loopar
	li	$t2, 1
LOOP1:
	andi 	$t0, $t0, 0x0
	subu	$a0, $a0, $t2	#subtraherar 1 fr?n a0
	ble	$a0, $0, DONE	#j?mf?r a0 och noll om de ?r lika s? hoppar det till DONE
	nop
	
LOOP2:
	beq	$t0, $t1, LOOP1	#j?mf?r t0 och v?rdet i t1 om de ?r lika s? g?r den tillbaka till LOOP1
	nop
	addi	$t0, $t0, 1	#l?gger till 1 till t0
	j	LOOP2		#b?rjar om p? loopen
	nop
DONE:
	#POP($ra)
	lw	$ra,0($sp)
	addi	$sp,$sp,4
	jr	$ra
	nop	


time2string:

	#PUSH($ra)
	addi	$sp,$sp,-4
	sw	$ra,0($sp)
	#PUSH($v0)
	addi	$sp,$sp,-4
	sw	$v0, 0($sp)
	#	addi	$sp,$sp,-4 	sw	$a0, $sp		#sparar alla v?rden
	addi	$sp,$sp,-4
	sw	$a0, 0($sp)
	move 	$a0, $a1	#flyttar talen som ?r i a1 till a0
	#addi	$sp,$sp,-4 	sw	$a1,0($sp)		#sparar a1
	srl	$a0, $a0, 12	#flytar siffrorna som har sparats i a0 tolv steg ?t h?ger f?r att komma ?t de fyra sista bitsen
	jal	hexasc		#anv?nder metoden hexasc
	nop
	lw	$a1,0($sp) 	
	addi	$sp,$sp,4		#flyttar tillbaka a1 d? den sist kom in i stacken
	lw	$a0,0($sp) 	
	addi	$sp,$sp,4		#flyttar tillbaka a0 f?r att f? adressen
	sb	$v0, 0x0($a0)	#sparar v?rdet som returneras till adressen som a0 pekar p?, f?rflyttad 0 steg
	addi	$sp,$sp,-4 
	sw	$a0, 0($sp)		#sparar a0 i stacken
	move	$a0, $a1	#flyttar v?rdet i a1 till a0 
	addi	$sp,$sp,-4 	
	sw	$a1,0($sp)		#sparar a1 i stacken
	srl	$a0, $a0, 8	#flytar siffrorna som har sparats i a0 tolv steg ?t h?ger f?r att komma ?t de 4 n?st sista bitsen
	jal	hexasc		#anv?nder metoden hexasc
	nop
	lw	$a1,0($sp) 	
	addi	$sp,$sp,4		#returnerar a1 fr?n stacken
	lw	$a0,0($sp) 	
	addi	$sp,$sp,4		#returnerar a0 fr?n stacken
	sb	$v0, 0x1($a0)	#sparar n?sta byte i minnesadressen som a0 pekar p?. f?rflyttad 1 steg
	andi	$t3, $t3, 0x0	#sparar en null byte i t3 och rensar den
	sb	$t3, 0x6($a0)	#l?gger till null byten i t3 i adressen som pekas p? av a0. f?rflyttat 5 steg.
	addi 	$t3, $t3, 0x3A	#l?gger till 0x3A i t3
	sb	$t3, 0x2($a0)	#l?gger till t3 i adressen som a0 pekar p?. F?rflyttad 2 steg.
	addi	$sp,$sp,-4 	
	sw	$a0, 0($sp)		#sparar a0 i stacken
	move	$a0, $a1	#flyttar v?rdet i a1 till a0
	addi	$sp,$sp,-4 	
	sw	$a1,0($sp)	#sparar a1 i stacken
	srl	$a0, $a0, 4	#flyttar v?rdet 4 steg ?t h?ger f?r att komma ?t n?sta tal
	jal	hexasc		#tillkallar metoden hexasc
	nop
	lw	$a1,0($sp) 	
	addi	$sp,$sp,4	#flyttar tillbaka v?rdet a1 fr?n stacken
	lw	$a0,0($sp) 	
	addi	$sp,$sp,4	#flyttar tillbaka a0 fr?n stacken
	sb	$v0, 0x3($a0)	#sparar byten med ascii karakt?ren i adressen som a0 pekar p?. F?rflyttat 3 steg.
	addi	$sp,$sp,-4 	
	sw	$a0, 0($sp)		#sparar a0 i stacken
	move	$a0, $a1	#flyttar v?rdet i a1 till a0
	addi	$sp,$sp,-4 
	sw	$a1,0($sp)	#sparar a1 i stacken
	jal	hexasc		#anropar metoden hexasc
	nop
	lw	$a1,0($sp) 	
	addi	$sp,$sp,4	#flyttar tillbaka v?rdet a1 fr?n stacken
	lw	$a0,0($sp) 	
	addi	$sp,$sp,4	#flyttar tillbaka v?rdet a0 fr?n stacken
	sb	$v0, 0x4($a0)	#sparar byten med den sista ascii karakt?ren i adressen som a0 pekar p?. F?rflyttat 4 steg.
	addi	$sp,$sp,-4
	sw	$a0, 0($sp)	#PUSH
	addi 	$a0, $a0, 0xD
	jal	hexasc
	nop
	lw	$a0,0($sp)
	addi	$sp,$sp,4
	sb	$v0, 0x5($a0)
	#POP($v0)		
	lw	$v0,0($sp)
	addi	$sp,$sp,4	#returnerar v0 fr?n stacken
	#POP($ra) 	
	lw	$ra,0($sp)
	addi	$sp,$sp,4	#returnerar ra fr?n stacken
	jr	$ra		#tillbaka till return adressen
	nop

  # timetemplate.asm
  # Written 2015 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

.macro	PUSH (%reg)
	addi	$sp,$sp,-4
	sw	%reg,0($sp)
.end_macro

.macro	POP (%reg)
	lw	%reg,0($sp)
	addi	$sp,$sp,4
.end_macro

	.data
	.align 2
mytime:	.word 0x5957
timstr:	.ascii "text more text lots of text\0"
two:	.ascii "TWO\0"
	.text
main:
	# print timstr
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	# wait a little
	li	$a0,2
	jal	delay
	nop
	# call tick
	la	$a0,mytime
	jal	tick
	nop
	# call your function time2string
	la	$a0,timstr
	la	$t0,mytime
	lw	$a1,0($t0)
	jal	time2string
	nop
	# print a newline
	li	$a0,10
	li	$v0,11
	syscall
	nop
	# go back and do it all again
	j	main
	nop
# tick: update time pointed to by $a0
tick:	lw	$t0,0($a0)	# get time
	addiu	$t0,$t0,1	# increase
	andi	$t1,$t0,0xf	# check lowest digit
	sltiu	$t2,$t1,0xa	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x6	# adjust lowest digit
	andi	$t1,$t0,0xf0	# check next digit
	sltiu	$t2,$t1,0x60	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa0	# adjust digit
	andi	$t1,$t0,0xf00	# check minute digit
	sltiu	$t2,$t1,0xa00	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x600	# adjust digit
	andi	$t1,$t0,0xf000	# check last digit
	sltiu	$t2,$t1,0x6000	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa000	# adjust last digit
tiend:	sw	$t0,0($a0)	# save updated result
	jr	$ra		# return
	nop

  # you can write your code for subroutine "hexasc" below this line
  #
    hexasc:
         li    $t0,15        #etablera plats med talet 15 i
         
         and    $t1,$t0,$a0    #anda ihop så allt utom de fyta sista bitsen blir 0
        
         li    $t2,0        #börjar på första för att jämföra med I
         li    $t3,0x30    #börjar på sista för att stoppa i output om jämförandet ger sant
         li    $t4,10
      loop09:
        beq    $t1,$t2, out
        nop
        
        addi    $t2,$t2,1
        addi    $t3,$t3,1
        
        bne    $t2,$t4, loop09        #loop slutar när den når 10
        nop
        li    $t3, 0x41        #för att ge output om jämförning är sann
        
      loopAF:
        beq    $t1,$t2, out
        nop
        
        addi    $t2,$t2,1
        addi    $t3,$t3,1
        j    loopAF
        nop
      out:
        
        add $v0,$0,$t3
        andi $v0,$v0, 0x7f
        jr    $ra
        nop
        
      delay:
          PUSH $s0
          PUSH $s1
          PUSH $s2
          li   $s1,2000
          li   $s2, 1
    dmsloop:
        li    $s0, 0
        beq     $a0,$0, dout
        nop
        addi     $a0, $a0, -1     #funk adda -1
        
    dwhileloop:
        beq    $s0,$s1, dmsloop
        nop
        addi    $s0, $s0, 1        #funk adda 1 t "i"
        
        j    dwhileloop
        nop
        j    dmsloop
        nop
    dout:
        POP $s2
        POP $s1
        POP $s0
        jr $ra
        nop
        
    

    time2string:
        PUSH $ra
        PUSH $s1
        PUSH $s2
        PUSH $s3
        
        add $s1, $a0, $0
        add $s2, $a1, $0
        
        srl  $a0, $s2, 12         #shifta till slutet
        jal  hexasc            # tiden i ASCII
        nop
        sb   $v0, 0($s1)
        
        
        srl  $a0, $s2, 8         #shifta till slutet
        jal  hexasc            # tiden i ASCII
        nop
        sb   $v0, 1($s1)
        
        li   $t4, 0x3A            #lägg in colon
        sb   $t4, 2($s1)
        
        srl  $a0, $s2, 4         #shifta till slutet
        jal  hexasc            # tiden i ASCII
        nop
        sb   $v0, 3($s1)
        
        
        srl  $a0, $s2, 0         #shifta inte men för min egen läsning
        jal  hexasc            # tiden i ASCII
        nop
        addi $t0, $0, 0x32
        bne $v0, $t0, NOTTWO
        nop
       
       #
       	li	$a0,10
	li	$v0,11
	syscall
	nop
	
	sb $0, 4($s1)
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	
	li $v0, 4
	la	$a0, two
	syscall
	nop
	
	li	$a0,2
	jal	delay
	nop
	
	la	$a0,mytime
	jal	tick
	nop
	
	la	$a0,timstr
	la	$t0,mytime
	lw	$a1,0($t0)
	jal	time2string
	nop
	
	j 	end
	nop

	NOTTWO: 
		sb   $v0, 4($s1)
        	sb   $0, 5($s1)
        
        	andi $s3, $a1, 0xFF
        
     
        	nop
        


    end:        #lägg tbaka värdena i s:n
        POP $s3
        POP $s2
        POP $s1
        POP $ra
        
        jr $ra
        nop

	
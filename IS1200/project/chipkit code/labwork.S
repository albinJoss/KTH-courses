  # labwork.S
  # Written 2015-2017 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1.
  # The macros PUSH and POP are in the public domain.
  # Please add your own code at the end of the file.

  #
  # Please keep the two macros PUSH and POP unchanged
  # ------ note to self: cd Download cd time4mips make -----
# make install TTYDEV=/dev/cu.usbserial-AJV9JY9X make
.macro	PUSH reg
	addi	$sp,$sp,-4
	sw	\reg,0($sp)
.endm

.macro	POP reg
	lw	\reg,0($sp)
	addi	$sp,$sp,4
.endm
  #
  # Please add your own code below this line
.data
.align 2
.text
.global enable_interrupt
    ei
jr  $ra
nop
.global delay
.global time2string
      # timetemplate.asm
      # Written 2015 by F Lundevall
      # Copyright abandonded - this file is in the public domain.

    mytime:    .word 0x5957
    timstr:    .ascii "text more text lots of text\0"
        .text
      # you can write your code for subroutine "hexasc" below this line
      #
        hexasc:
         li    $t0,15        		#etablera plats med talet 15 i
         
         and    $t1,$t0,$a0    		#anda ihop så allt utom de fyta sista bitsen blir 0
        
         li    $t2,0        		#börjar på första för att jämföra med I
         li    $t3,0x30    		#börjar på sista för att stoppa i output om jämförandet ger sant
         li    $t4,10
      loop09:
        beq    $t1,$t2, out
        nop
        
        addi    $t2,$t2,1
        addi    $t3,$t3,1
        
        bne    $t2,$t4, loop09        	#loop slutar när den når 10
        nop
        li    $t3, 0x41        		#för att ge output om jämförning är sann
        
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
          li   $s1,3500
          li   $s2, 1
    dmsloop:
        li    $s0, 0
        beq     $a0,$0, dout
        nop
        addi     $a0, $a0, -1     	#funk adda -1
        
    dwhileloop:
        beq    $s0,$s1, dmsloop
        nop
        addi    $s0, $s0, 1        	#funk adda 1 t "i"
        
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
        
        srl  $a0, $s2, 12         	#shifta till slutet
        jal  hexasc            		# tiden i ASCII
        nop
        sb   $v0, 0($s1)
        
        
        srl  $a0, $s2, 8         	#shifta till slutet
        jal  hexasc            		# tiden i ASCII
        nop
        sb   $v0, 1($s1)
        
        li   $t4, 0x3A            	#lägg in colon
        sb   $t4, 2($s1)
        
        srl  $a0, $s2, 4         	#shifta till slutet
        jal  hexasc            		# tiden i ASCII
        nop
        sb   $v0, 3($s1)
        
        
        srl  $a0, $s2, 0         	#shifta inte men för min egen läsning
        jal  hexasc            		# tiden i ASCII
        nop
        sb   $v0, 4($s1)
        
        sb   $0, 5($s1)
        
        andi $s3, $a1, 0xFF
        
        #beq $s3, $0, Ding
        nop

    end:        			#lägg tbaka värdena i s:n
        POP $s3
        POP $s2
        POP $s1
        POP $ra
        
        jr $ra
        nop


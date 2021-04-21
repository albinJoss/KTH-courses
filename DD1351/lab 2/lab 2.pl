verify(InputFileName):-
	see(InputFileName),
	read(Prems), read(Mal), read(Bevis),
	seen,
	%writeln(Prems), writeln(Mal), writeln(Bevis), writeln(' '),
	kontrollera_mal(Bevis,Mal),											%kollar om bevisets sista rad är lika med målet som lästs in
	!, kontrollera_bevis(Prems, Bevis, Bevis, []), !.

hitta_sista([Huvud | []], Huvud).		%Basklausul för hitta_sista kommer att användas när Huvud är sista elementet.
hitta_sista([_ | Svans], Huvud):-		%används när det finns element kvar i listan. Ignorer då vad det finns för värde innan och går sen vidare rekursivt för att hitta sista elementet.
	hitta_sista(Svans, Huvud).
	
hitta_varde([_, Huvud, _], Huvud). 		%Används för att matcha ett värde och hämta det

hitta_forsta([Huvud | _], Huvud). 		%Hittar första saken i listan och sedan ger den variabeln som skickas med det värdet eller kollar om den matchar det som blir medskickat i den andra variabeln.


kontrollera_mal(Bevis, Mal):-
	hitta_sista(Bevis, Rad), 			%writeln(Rad),%
	hitta_varde(Rad, Varde), 			%writeln(Varde), writeln(Mal),
	Mal = Varde, !.						%jämför målet som lästs med sista det som hämtats ur sista raden i beviset

kontrollera_premis(_, []):- !, fail.					%basfall så att om premissen som skickats med är tom failar operationen 
kontrollera_premis(Varde, [Varde | _]).					%
kontrollera_premis(Varde, [_ | Svans]) :- 				%om en premis 
%write('Varde: '), writeln(Varde), write('Svans: '), writeln(Svans),
!, kontrollera_premis(Varde, Svans).

aktiv_lada(_, [], _) :-																						%Basfall, om lådan är tom vid något tillfälle så kommer funktionen att faila.
!, fail.
	
aktiv_lada([Nummer, Varde, _], [[[Nummer, Varde, _] | Ladsvans] | _], [[Nummer, Varde, _] | Ladsvans]).		%Letar efter en del av listan som matchar en låda. Den tredje variablen som skickas med kommer få det värdet.

aktiv_lada([Nummer, Varde, _], [_ | Svans], Lada):-															%Rekursivt åkallar sig själv tills den matchar signaturen av en lada i den listan som skickas med. 
!, aktiv_lada([Nummer, Varde, _], Svans, Lada).


kontrollera_bevis(_, _, [], _) :- !.																		%Basklausul. tillkallas när Svans är tom och vi är färdiga med beviset.

%kontrollerar att premiss är rätt
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, premise] | Svans], Scope):-
	kontrollera_premis(Varde, Prems),																		%kallar kontrollera_premis
	%writeln('Premise'),
	%write('Varde: '), writeln(Varde), write('Prems: '), writeln(Prems),
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, premise] | Scope]).								%kallar kontrollera bevis rekursivt


%Kollar ett antagande
kontrollera_bevis(Prems, Bevis, [[[Rad, Varde, assumption] | Ladsvans] | Svans], Scope):-					
	%writeln('assumption'),
	kontrollera_bevis(Prems, Bevis, Ladsvans, [[Rad, Varde, assumption] | Scope]),							%kallar kontrollera bevis rekursivt för att kolla lådans innehåll
	kontrollera_bevis(Prems, Bevis, Svans, [[[Rad, Varde, assumption] | Ladsvans] | Scope]).				%fortsätter med resten av beviset

%Kopierar
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, copy(X)] | Svans], Scope):-
	member([X, Gammalr, _], Scope),																			%identifierar den rad som ska kopieras
	%write('copy'),																
	Gammalr = Varde, !,																						%jämför innehållet i raden som kopierat och raden som utfört kopierandet
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, copy(X)] | Scope]).								%kallar kontrollera bevis rekursivt

%introducerar ett and element
kontrollera_bevis(Prems, Bevis, [[Rad, and(A,B), andint(X,Y)]| Svans], Scope):-
	%writeln('andint'),
	member([X, A, _], Scope),																				%kollar att första termen som and ska införas mellan finns på utsedda raden
	member([Y, B, _], Scope),																				%kollar att andra termen som and ska införas mellan finns på utsedda raden
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, and(A,B), andint(X,Y)] | Scope]).						%kallar kontrollera bevis rekursivt

%and el 1
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, andel1(X)] | Svans], Scope):-
	%writeln('andel1'),
	member([X, and(Varde, _a), _], Scope),																	%Letar efter raden X och kontrollerar att den innehåller ett and vars första plats ockuperas
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, andel1(X)] | Scope]).							%av Varde och att det finns ett värde på den andra platsen men det spelar ingen roll vad det värdet är.

	
%and el 1
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, andel2(X)] | Svans], Scope):-
	%writeln('andel2'),
	member([X, and(_a, Varde), _], Scope),																	%Letar efter raden X och kontrollerar att den innehåller ett and vars andra plats ockuperas
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, andel2(X)] | Scope]).							%kallar kontrollera bevis rekursivt

%or int
kontrollera_bevis(Prems, Bevis, [[Rad, or(A,B), orint1(X)] | Svans], Scope):-
	%writeln('orint1'),
	member([X, A, _], Scope),																				%Kollar att första termen i radens orsats finns på raden som refereras till
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, or(A,B), orint1(X)] | Scope]).							%kallar kontrollera bevis rekursivt

%or int 2
kontrollera_bevis(Prems, Bevis, [[Rad, or(A,B), orint2(X)] | Svans], Scope):-
	%writeln('orint2'),
	member([X, B, _], Scope),																				%Kollar att andra termen i radens orsats finns på raden som refereras till
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, or(A,B), orint2(X)] | Scope]).							%kallar kontrollera bevis rekursivt

%or el
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, orel(X,Y,U,V,W)] | Svans], Scope):-
    %writeln('orel'),
    member([X, or(A,B), _], Scope),																			%namnger termerna som oras till A och B samt kollar att det finns en or att eliminera
    aktiv_lada([Y, A, _], Scope, Lada1),																	%letar efter en låda som börjar med A på rad Y
    aktiv_lada([V, B, _], Scope, Lada2),																	%letar efter en låda som börjar med B på rad V
    hitta_forsta(Lada1, [Y, A, _]),																			%kollar att lådan  börjar med A på rätt rad
    hitta_forsta(Lada2, [V, B, _]),																			%kollar att lådan  börjar med B på rätt rad
    hitta_sista(Lada1, [U, Varde, _]),																		%kollar om sista raden båda lådorna är dessamma
    hitta_sista(Lada2, [W, Varde, _]),																		
    !, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, orel(X,Y,U,V,W)] | Scope]).						%kallar kontrollera bevis rekursivt

%impint
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, impint(X,Y)] | Svans], Scope):-
    %writeln('impint'),
    aktiv_lada([X, A, _], Scope, Box),																		%letar efter en låda som börjar på rad X
    hitta_forsta(Box, [X, A, _]),																			%letar efter första raden i lådan
    hitta_sista(Box, [Y, B, _]),																			%letar efter sista raden i lådan 
    !, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, impint(X,Y)] | Scope]).							%kallar kontrollera bevis rekursivt

%impel
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, impel(X,Y)] | Svans], Scope):-						
	%writeln('impel'),
	member([X, X2, _], Scope),																				%namnger innehållet på första raden 
	member([Y, imp(X2, Varde), _], Scope), !,																%kollar att rad Y innehåller en implikation mellan innehållet i rad X 
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, impel(X,Y)] | Scope]).							%kallar kontrollera bevis rekursivt

%negint
kontrollera_bevis(Prems, Bevis, [[Rad, neg(A), negint(X,Y)]| Svans], Scope):-		
	%writeln('negint'),
	aktiv_lada([X, A, _], Scope, Lada), 																	%Letar efter en låda som matchar signaturen i Scope. Lada kommer att få det som värde.
	hitta_forsta(Lada, [X, A, _]),																			%kollar att första raden i lådan är rad X som innehåller A
	hitta_sista(Lada, [Y, cont, _]),																		%kollar att sista raden i lådan är Y 
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, neg(A), negint(X,Y)] | Scope]).						%kallar kontrollera bevis rekursivt

%negel
kontrollera_bevis(Prems, Bevis, [[Rad, cont, negel(X,Y)] | Svans], Scope):-									
	%writeln('negel'),
	member([X, X2, _],Scope),																				%kollar att raden X fins och namnger innehållet
	member([Y, neg(X2), _], Scope),																			%kollar att rad Y finns och innehåller en negation av innehållet i X
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, cont, negel(X,Y)] | Scope]).							%kallar kontrollera bevis rekursivt

%contel
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, contel(X)]| Svans], Scope):-			
	%writeln('contel'),
	member([X, cont, _], Scope),																			%kollar att raden som refereras till innehåller cont
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, contel(X)] | Scope]).							%kallar kontrollera bevis rekursivt

%negnegint
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, negnegint(X)] | Svans], Scope):-		
	%writeln('negnegint'),
	member([X, Copy, _], Scope),																			%kollar att rad X finns och namnger innehållet
	neg(neg(Copy)) = Varde,																					%kollar att en dubbelnegation av rad X inehåll är desamma som radens innehåll
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, negnegint(X)] | Scope]).						%kallar kontrollera bevis rekursivt

%negnegel
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, negnegel(X)] | Svans], Scope):-		
	%writeln('negnegel'),
	member([X, neg(neg(Varde)), _], Scope),																	%kollar att X inehåller en dubbel negation med värdet i
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, negnegel(X)] | Scope]).							%kallar kontrollera bevis rekursivt

%mt
kontrollera_bevis(Prems, Bevis, [[Rad, neg(Varde), mt(X,Y)] | Svans], Scope):-
    %writeln('mt'),
    member([X, imp(Varde,B), _], Scope),																	%kollar att X inehåller en implikation melln radens värde och något som namnges som B
    member([Y, neg(B), _], Scope),																			%kollar attrad Y innehåller en negation av B
    kontrollera_bevis(Prems, Bevis, Svans, [[Rad, neg(Varde), mt(X,Y)] | Scope]).							%kallar kontrollera bevis rekursivt

%pbc
kontrollera_bevis(Prems, Bevis, [[Rad, Varde, pbc(X,Y)] | Svans], Scope):-
    %writeln('pbc'),
    aktiv_lada([X, neg(Varde), _], Scope, Lada),															%letar efter en låda som börjar på rad X och som innehåller en negation av värdet i raden
    hitta_forsta(Lada, [X, neg(Varde), _]),																	%Kollar att första raden i lådan stämmer med X och negation
    hitta_sista(Lada, [Y, cont, _]),																		%kollar att sista raden innehåller en cont
    !, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, Varde, pbc(X,Y)] | Scope]).							%kallar kontrollera bevis rekursivt

%lem
kontrollera_bevis(Prems, Bevis, [[Rad, or(A,B), lem] | Svans], Scope):-
	%writeln('lem'),
	A = neg(B) ; B = neg(A), !,																				%kollar att A är en negation av B och B är en negation av A 
	!, kontrollera_bevis(Prems, Bevis, Svans, [[Rad, or(A,B), lem] | Scope]).								%kallar kontrollera bevis rekursivt


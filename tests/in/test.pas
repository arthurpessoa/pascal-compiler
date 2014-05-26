PROGRAM teste;


function fatorial( n : integer; ) : integer
  begin
  if  n <= 1 
  then
    return 1;
  else
    return n*fatorial(n-1);
  endif;
  end

procedure print( a, b : integer; ch : string; )
  var i : integer;
  begin
  write(ch);
  while i <= 5  do
    write( a / b );
    i := i + 1;
    endwhile;
  while i <= 50 do
    i := i + 1;
  endwhile;
  end

BEGIN
	Write(fatorial(5));
	print(1,2,'a');
END
.
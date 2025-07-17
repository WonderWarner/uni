library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity clk_divider is
    port (
        clk      : in  std_logic;
        reset_n  : in  std_logic;
        clk_out  : out std_logic
    );
end entity clk_divider;

architecture rtl of clk_divider is
    constant DIVISOR : integer := 500000;  -- 50MHz / 100Hz
    signal counter   : integer range 0 to DIVISOR := 0;
    signal pulse     : std_logic := '0';
begin

    process(clk, reset_n)
    begin
        if reset_n = '0' then     -- negatív élvezérelt
            counter <= 0;
            pulse <= '0';
        elsif rising_edge(clk) then
            if counter = DIVISOR - 1 then   -- eggyel hamarabb kell, a regiszter írás miatt
                counter <= 0;
                pulse <= '1';
            else
                counter <= counter + 1;
                pulse <= '0';
            end if;
        end if;
    end process;

    clk_out <= pulse;

end architecture rtl;


package com.langandlib;

import com.langandlib.language.BetterDiamond;
import com.langandlib.optional.OptionalIfPresentOrElse;
import com.langandlib.optional.OptionalOr;
import com.langandlib.optional.OptionalStream;
import com.langandlib.stream.FindGitConflict;
import com.langandlib.stream.NewCollectors;
import com.langandlib.stream.OfNullable;
import com.langandlib.stream.TakeDropWhile;

public class Testrun {
    public static void main(String... args) throws Exception {
        System.out.println("run some sample code");
        BetterDiamond.main();
        OptionalIfPresentOrElse.main();
        OptionalOr.main();
        OptionalStream.main();
        FindGitConflict.main();
        NewCollectors.main();
        OfNullable.main();
        TakeDropWhile.main();
    }

}

package fi.reaktor.android.rx.jackson.deser;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

public class TotallylazyModule extends Module {

    @Override
    public String getModuleName() {
        return "TotallylazyModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(Module.SetupContext context) {
        context.addDeserializers(new TotallylazyDeserializers());
    }

    @Override
    public int hashCode() {
        return TotallylazyModule.class.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}

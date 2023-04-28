package com.architecture.app.viewModels;

public class ArchitectureTypeNode {
    /** Text representation for the client */
    public final String label;

    /** The text value used in the model of neural network */
    public final String value;

    /** Some description that might be useful */
    public final String description;

    /** Link to the preview photo */
    public final String preview;

    /** External link to an article about the architectural type */
    public final String moreInfoLink;

    /** Count of how many times this type was found by used */
    public final int foundCount;

    public ArchitectureTypeNode(String label, String value, String description, String preview, String moreInfoLink, int foundCount) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.preview = preview;
        this.moreInfoLink = moreInfoLink;
        this.foundCount = foundCount;
    }
}

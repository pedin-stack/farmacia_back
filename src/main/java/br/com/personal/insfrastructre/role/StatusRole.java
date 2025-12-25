    package br.com.personal.insfrastructre.role;

    import lombok.AllArgsConstructor;
    import lombok.Getter;

    @AllArgsConstructor
    @Getter
    public enum StatusRole {

        URGENTE("Urgente"),
        ATENCAO("Atenção"),
        NORMAL("Normal"),;

        private final String label;
    }
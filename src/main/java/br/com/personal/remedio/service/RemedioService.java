package br.com.personal.remedio.service;

import br.com.personal.insfrastructre.role.StatusRole;
import br.com.personal.remedio.dto.RemedioRequestDTO;
import br.com.personal.remedio.entity.Remedio;
import br.com.personal.remedio.repository.RemedioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemedioService {

    private final RemedioRepository remedioRepository;

    public Page<Remedio> findAll(Pageable pageable) {
        return remedioRepository.findAll(pageable);
    }

    public Remedio findById(Long id) {
        return remedioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remédio não encontrado com o ID: " + id));
    }

    @Transactional
    public Remedio save(Remedio remedio) {
        remedio.setId(null); //forçar ao repository criar um novo objeto e não atualizar um existeente
        calcularProximaCompraEStatus(remedio);
        return remedioRepository.save(remedio);
    }

    @Transactional
    public Remedio update(Long id, RemedioRequestDTO dto) {
        Remedio remedio = findById(id);

        remedio.setNome(dto.getNome());
        remedio.setQuantidade(dto.getQuantidade());
        remedio.setUsoDiario(dto.getUsoDiario());

        // Recalcula tudo ao atualizar
        calcularProximaCompraEStatus(remedio);

        return remedioRepository.save(remedio);
    }

    @Transactional
    public void delete(Long id) {
        Remedio remedio = findById(id);
        remedioRepository.delete(remedio);
    }


    private void calcularProximaCompraEStatus(Remedio remedio) {

        if (remedio.getQuantidade() > 0 && remedio.getUsoDiario() > 0) {
            long diasDuracao = (long) (remedio.getQuantidade() / remedio.getUsoDiario());
            LocalDate dataPrevista = LocalDate.now().plusDays(diasDuracao);
            remedio.setProxCompra(dataPrevista);
        } else {
            remedio.setProxCompra(LocalDate.now());
        }

        definirStatus(remedio);
    }

    private void definirStatus(Remedio remedio) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataCompra = remedio.getProxCompra();

        long diasRestantes = ChronoUnit.DAYS.between(hoje, dataCompra);

        // Se a quantidade for 0, é urgente independente da data
        if (remedio.getQuantidade() <= 0 || diasRestantes <= 3) {
            remedio.setStatus(StatusRole.URGENTE);
        } else if (diasRestantes <= 5) {
            // Entre 4 e 5 dias
            remedio.setStatus(StatusRole.ATENCAO);
        } else {
            // 6 ou mais dias
            remedio.setStatus(StatusRole.NORMAL);
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void atualizarEstoqueDiario() {
        System.out.println("--- Iniciando atualização automática de estoque ---");

        List<Remedio> remedios = remedioRepository.findAll();

        for (Remedio remedio : remedios) {

            double novaQuantidade = remedio.getQuantidade() - remedio.getUsoDiario();

            if (novaQuantidade < 0) {
                novaQuantidade = 0;
            }

            remedio.setQuantidade(novaQuantidade);
            calcularProximaCompraEStatus(remedio);
        }

        // Salva todos de uma vez (mais performático que salvar um por um dentro do loop)
        remedioRepository.saveAll(remedios);

        System.out.println("--- Estoque atualizado com sucesso! ---");
    }

}

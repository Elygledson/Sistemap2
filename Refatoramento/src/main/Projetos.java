package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import Colaboradores.Colaboradores;
import Producao.ProducaoAcademica;
import main.StatusdoProjeto.AlterarStatus;
import main.StatusdoProjeto.TipoStatus;


public class Projetos implements Comparable<Projetos> {
    
    private String Titulo;
	public String A_financiadora;
	private String Objetivo;
	private String Descricao;
	private String Status;
    private String DataInicio, DataTermino;
    private double Valor;
    public int q_professor; /* Quantidade de professores associados */
    public ArrayList<Colaboradores> participantes;
    public ArrayList<ProducaoAcademica> producao; /* publicações ou orientações associadas ao projeto */
    static Scanner input = new Scanner(System.in);

    public Projetos(String titulo, String DataInicio, String DataTermino, String a_financiadora, double valor,
            String objetivo, String descricao, String status) {
        this.Titulo = titulo;
        this.DataInicio = DataInicio;
        this.DataTermino = DataTermino;
        this.A_financiadora = a_financiadora;
        this.Valor = valor;
        this.Objetivo = objetivo;
        this.Descricao = descricao;
        this.Status = status;
        this.participantes = new ArrayList<Colaboradores>();
        this.producao = new ArrayList<ProducaoAcademica>();
    }

    public String getTitulo()
    {
        return this.Titulo;
    }

    public String getDataInicio() {
        return this.DataInicio;
    }

    public String getDataTermino() {
        return this.DataTermino;
    }

    public String getStatus() {
        return this.Status;
    }

    public double getValor()
    {
        return this.Valor;
    }

    public int getNumerodeProfessores(){
        int i,q_professor;
        for (i = 0, q_professor = 0; i < this.participantes.size(); i++) {
            if (this.participantes.get(i).getTipo().equals("PROFESSOR"))
                q_professor++;
        }
        return q_professor;
    }

    public int getNumeroParticipantes() {
        return this.participantes.size();
    }

    public int getPublicacoes() {
        return this.producao.size();
    }

    public String getFinanciadora()
    {
        return this.A_financiadora;
    }

    public String getDescricao()
    {
        return this.Descricao;
    } 

    public String getObjetivo()
    {
        return this.Objetivo;
    }

    public static boolean Verificacao(Colaboradores colaborador, String Titulo) {
        for (int i = 0; i < colaborador.projetos.size(); i++) {
            if (colaborador.projetos.get(i).Titulo.equals(Titulo)) {
                return true;
            }
        }
        return false;
    }

    public static void Sort(ArrayList<Projetos> projetos) {
        Collections.sort(projetos);
    }

    public Date getDateTime() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
        Date data = (Date) formato.parse(this.DataTermino);
        return data;
    }

    @Override
    public int compareTo(Projetos projeto) {
        try {
            return getDateTime().compareTo(projeto.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
      }

    public void setStatus()
    {
        System.out.println("\nAlterar status para:");
        System.out.println("[1] - Em andamento");
        System.out.println("[2] - Concluído\n");
        var opcao = Integer.parseInt(input.nextLine());
        while(opcao > 2 || opcao <= 0){
            System.out.println("Comando inválido!Tente novamente");
            opcao = Integer.parseInt(input.nextLine());
        }
        TipoStatus tipostatus = TipoStatus.values()[opcao - 1];

         AlterarStatus Status = tipostatus.ObterStatus();
         int q_geral = Math.abs(this.getNumerodeProfessores() - this.getNumeroParticipantes());
         Status.Restricao01(this.getNumerodeProfessores(),q_geral);
         Status.Restricao02(this.producao.isEmpty());
         String status  = Status.returnStatus();
         if(!status.equalsIgnoreCase("Status não alterado"))
            this.Status = status;

         System.out.printf("quantidade de professores: %d\nquantidade de participantes tirando professores: %d\ntipo de status atual: %s",
         this.getNumerodeProfessores(),q_geral,status);
         return;
    }

    public void ListarPublicacoes(String tipo)
    {
        ProducaoAcademica.Sort(this.producao);
        for(int i = 0;i < this.producao.size();i++)
        {   
            if(this.producao.get(i).Tipo.equals(tipo)){

            for(int j = 0;j < this.producao.get(i).Projetos.size();j++)
            System.out.printf("\n[NOME DO PROJETO] %s\n",this.producao.get(i).getProjeto(j));

            for(int j = 0;j < this.producao.get(i).Autores.size();j++)
            System.out.printf("[NOME DO(S) AUTOR(ES)] %s\n",this.producao.get(i).getAutores(j));

             System.out.printf("[TITULO] -> %s\n[NOME DA CONFERÊNCIA] -> %s\n[TIPO] -> %s \n[LOCAL] -> %s\n[ANO] -> %d\n\n",this.producao.get(i).getTitulo(),this.producao.get(i).getNome(),
             this.producao.get(i).getTipo(),this.producao.get(i).getLocal(),this.producao.get(i).getAno());
            }
        }
    }

    public void ListarOrientacoes(String tipo)
    {
            for(int i = 0;i < this.producao.size();i++)
            {   
                if(this.producao.get(i).Tipo.equals(tipo)){
                System.out.printf("\n[TITULO] -> %s\n[TIPO] -> %s\n[ANO] -> %d\n",this.producao.get(i).getTitulo(),this.producao.get(i).getTipo(),this.producao.get(i).getAno());

                for(int j = 0;j < this.producao.get(i).Projetos.size();j++)
                    System.out.printf("[NOME DO PROJETO] %s\n",this.producao.get(i).getProjeto(j));
                
                for(int j = 0;j < this.producao.get(i).Alunos.size();j++)
                    System.out.printf("[NOME DO ALUNO]: %s\n",this.producao.get(i).getAluno(j));

                }
            }
    }

    public void ListarProjeto() {
        System.out.println("\nInformações do projeto adicionais:\n");
        System.out.printf(
                "[STATUS] -> ''%s'' [TÍTULO] -> %s [DATA DE INÍCIO] -> %s [DATA DE TERMINO] -> %s [FINANCIADORA] -> %s [VALOR] -> R$ %.2f \n",
                this.Status, this.Titulo, this.DataInicio, this.DataTermino, this.A_financiadora, this.Valor);
        System.out.printf("[DESCRIÇÃO]\n%s\n[OBJETIVO]\n%s\n", this.Descricao, this.Objetivo);

        if (getNumeroParticipantes() == 0) {
            System.out.println("\nNão há colaboradores associados ao projeto.");
        }

        for (int i = 0; i < this.participantes.size(); i++) {
            System.out.println("\nDados do colaborador:\n");
            System.out.printf("[NOME]  ->%s \n[TIPO]  ->[%s|%s]\n", this.participantes.get(i).getNome(),
                    this.participantes.get(i).getTipo(), this.participantes.get(i).getGrau());
        }
        if(this.producao.size() == 0)
        {
            System.out.printf("\nAVISO: não há produções acadêmicas associadas ao projeto\n");
        }
        else{
        System.out.println("\nTipos de produção ordenados em ordem descrescente:\n");
        System.out.println("\nPublicações:\n");
        ListarPublicacoes("PUBLICAÇÃO");
        System.out.println("\nOrientações:\n");
        ListarOrientacoes("ORIENTAÇÃO");
        }

    }
}
